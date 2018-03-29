import re

unit_flag = "@unit"
sql_key = ['PRIMARY KEY', 'UNIQUE', 'CHECK']
type_map = {
    'VARCHAR':'String',
    'BIGINT':'Long',
    'BOOLEAN':'Boolean',
    'JSONB':'JSONObject',
    'timestamp':'Date',
    'timestamptz':'Date',
    'DOUBLE PRECISION':'Double',
    'INT4RANGE':'HermesRange<Integer>',
    'INT8RANGE':'HermesRange<Long>',
    'INT':'Integer',
    'TSTZRANGE':'HermesRange<Date>',
    'DATERANGE':'HermesRange<HermesDay>',
    'DATE':'HermesDay'
}

def analyse_type(src):
    type_got = {}
    for key in type_map:
        
        if src.startswith(key):
            if key == 'DATE':
                if src.startswith('DATERANGE'):
                    continue
            type_got['base'] = type_map[key]
            if type_got['base'].startswith("HermesRange"):
                type_got['gen'] = type_got['base']
                type_got['base'] = type_got['base'].split('<')[1].split('>')[0]
            elif src.find('[]') != -1:
                type_got['gen'] = 'List<'+type_got['base']+'>'
            else:
                type_got['gen'] = type_got['base']
            if type_got['gen'].find('<') != -1:
                type_got['T'] = True
                m = type_got['gen'].split('<')
                type_got['tyg'] = m[0]+'<?>'
            else:
                type_got['T'] = False
                type_got['tyg'] = type_got['gen']
            break
    return type_got

def analyse_col(col):
    col_cont = {}
    if col.find('DEFAULT') != -1:
        col_cont['auto'] = True
    else:
        col_cont['auto'] = False
    if col.find('PRIMARY KEY') != -1:
        col_cont['ret'] = True
    else:
        col_cont['ret'] = False
    col_elem = col.split(' ')
    pattern = re.compile('^\\s*([^ ]+) ([^,]+),?(.*)$')
    res = pattern.match(col)
    col_cont['name'] = res.group(1)
    col_cont['type'] = analyse_type(res.group(2))
    col_cont['attr'] = []
    if col.find('--') != -1:
        ext = col[col.find('--')+3:]
        ext_set = ext.split('@')
        for ext_item in ext_set:
            es = ext_item.split(' ')
            if es[0] in ['pub_ro', 'ro', 'ne', 'detail', 'wo']:
                col_cont['attr'].append(es[0])
            elif es[0] == 'Enum':
                col_cont['enum'] = es[1]
            elif es[0] == 'auto':
                col_cont['auto'] = True
    return col_cont

def analyse_block(parm, hu, model):
    tbl_cont = {}
    pattern = re.compile('.*\\s(\\S+)\\s*\\(')
    name = pattern.match(parm).group(1)
    if name.find('.') != -1:
        m = name.split('.')
        tbl_cont['prefix'] = m[0]
        name = m[1]
    else:
        tbl_cont['prefix']= ''
    tbl_cont['tag'] = name
    if hu==None:
        tbl_cont['hu'] = name
    else:
        tbl_cont['hu'] = hu
    if model==None:
        tbl_cont['model'] = None
    else:
        tbl_cont['model'] = model
    col_body = parm[parm.find('(')+1:parm.rfind(')')]
    col_body = re.sub('  +',' ',col_body)
    cols = col_body.split('\n')
    tbl_cont['cols'] = []
    for col in cols:
        if len(col)<5:
            continue
        col = re.sub('^ +','', col)
        skip = False
        for s_key in sql_key:
            if col.startswith(s_key):
                skip = True
        if skip:
            continue
        tbl_cont['cols'].append(analyse_col(col))
    return tbl_cont

def analyse_file(file_name):
    f_sql = open(file_name)
    read_set = f_sql.readlines()
    block_text = ""
    in_analyse = False
    tpls = []
    hu_found = None
    model_found = None
    for line in read_set:
        line = line.replace('`', '')
        if line.find(unit_flag) != -1:
            in_analyse = True
            block_text = ""
            hu_found = re.findall('\\s+#([^\\s]+)',line)
            if hu_found:
                hu_found = hu_found[0]
            else:
                hu_found = None
            model_found = re.findall('\\s+_([^\\s]+)',line)
            if model_found:
                model_found = model_found[0]
            else:
                model_found = None
            continue
        if in_analyse:
            block_text = block_text + line
            if line.find(';') != -1:
                tpls.append(analyse_block(block_text, hu_found, model_found))
                in_analyse = False
                hu_found = None
    return tpls