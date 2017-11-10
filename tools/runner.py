import analyse
import tpl

from hermes_conf import *

tbl_offset = 0
import re

import_map = {
    'Date':'java.util.Date',
    'List':'java.util.List',
    'ArrayList':'java.util.ArrayList',
    'Arrays':'java.util.Arrays',
    'JSONObject':'com.alibaba.fastjson.JSONObject',
    'HermesArray':'com.clearso.hermes.HermesArray',
    'HermesRange':'com.clearso.hermes.types.HermesRange',
    'HermesMap':'com.clearso.hermes.HermesMap',
    'HermesDay':'com.clearso.hermes.types.HermesDay'
}

sw = [False]

def java_var(col, upper_first = False):
    word_set = col.split('_')
    res_str = word_set[0]
    if upper_first:
        res_str = res_str[0].upper() + res_str[1:]
    for i in range(1 , len(word_set)):
        if word_set[i][0] >= 'a' and word_set[i][0] <= 'z':
            res_str = res_str + word_set[i][0].upper() + word_set[i][1:]
    return res_str

def map_model(unit):
    b_dict = {}
    try:
        b_dict[0] = [{'model':java_var(unit['model'], True)}]
    except:
        return None
    b_dict[2] = [{'unit':java_var(unit['hu'], True)}]
    b_dict[1] = []
    b_dict[3] = []
    b_dict[4] = []
    b_dict[5] = []
    b_dict[7] = []
    b_dict[8] = []
    b_dict[9] = []
    b_dict[10] = []
    b_dict[11] = []
    b_dict[12] = []
    for col in unit['cols']:
        if not col['auto']:
            b_dict[1].append({'type':col['type']['gen'],
                              'col_name':java_var(col['name'])})
            b_dict[3].append({'var':java_var(col['name'], True),
                              'trans':java_var(col['name'])})
        if col['ret']:
            b_dict[4].append({'ret_col':java_var(col['name'], True)})
            b_dict[5].append({'ret_type':java_var(col['type']['gen'])})
            b_dict[10].append({'ret_var':java_var(col['name'])})
        if (not 'detail' in col['attr']) and (not 'ne' in col['attr']) and (not 'wo' in col['attr']):
            b_dict[7].append({'unit':java_var(unit['hu'], True),
                              'col':java_var(col['name'], True)})
            b_dict[8].append({'cname':col['name'],
                              'unit':java_var(unit['hu'], True),
                              'col':java_var(col['name'], True)})
        if (not 'ne' in col['attr']) and (not 'ro' in col['attr']) and not col['ret']:
            b_dict[9].append({'unit':java_var(unit['hu'], True),
                              'col':java_var(col['name'], True),
                              'key':col['name']})
        if (not 'ne' in col['attr']) and (not 'wo' in col['attr']):
            b_dict[11].append({'unit':java_var(unit['hu'], True),
                              'col':java_var(col['name'], True)})
            b_dict[12].append({'cname':col['name'],
                               'unit':java_var(unit['hu'], True),
                               'col':java_var(col['name'], True)})
            
    return b_dict

def map_hu(unit):
    global tbl_offset
    print 'unit -- '+unit['hu']
    b_dict = {}
    b_dict[0] = [{
            'model':java_var(unit['hu'], True)
            }]
    b_dict[1] = []
    b_dict[3] = []
    b_dict[4] = []
    b_dict[6] = []
    b_dict[8] = []
    b_dict[9] = []
    b_dict[11] = []
    b_dict[12] = []
    b_dict[13] = []
    sw[0] = False
    vindex = 0
    for col in unit['cols']:
        b_dict[1].append({
                'type':col['type']['gen'],
                'var':java_var(col['name'], True)
                })
        b_dict[3].append({
                'var':java_var(col['name'], True),
                'vindex':str(vindex)
                })
        b_dict[4].append({
                'var':java_var(col['name'], True)
                })
        b_dict[6].append({
                'col_name':col['name']
                })
        b_dict[8].append({
                'var':java_var(col['name'], True)
                })
        if col['type']['T']:
            sw[0] = True
            
        b_dict[11].append({
                'var':java_var(col['name'], True)
                })
        if True:
            b_dict[9].append({
                'var':java_var(col['name'], True),
                'tyg':col['type']['tyg'],
                'type':col['type']['gen']
            })
            for i in range(12, 14):
                if i == 12:
                    if 'ro' in col['attr']:
                        continue;
                    dmap = analyse.json_map
                else:
                    dmap = analyse.rs_map
                mp_dict = None
                for k in dmap:
                    if col['type']['gen'].startswith(k):
                        import copy
                        mp_dict = copy.deepcopy(dmap[k])
                        mp_dict['st'] = mp_dict['st'].replace('$type', col['type']['base'])
                        mp_dict['type'] = mp_dict['type'].replace('$type', col['type']['base'])
                        mp_dict['ed'] = mp_dict['ed'].replace('$type', col['type']['base'])
                        break
                if mp_dict == None:
                    mp_dict = {
                        'st':'',
                        'ed':'',
                        'type':col['type']['base']+'('
                    }
                mp_dict['var'] = java_var(col['name'], True)
                mp_dict['col'] = col['name']
                b_dict[i].append(mp_dict)
        vindex = vindex + 1
    b_dict[2] = [{
            'offset':tbl_offset
            }]
    b_dict[5] = [{
            'cnum':vindex
            }]
    b_dict[7] = [{
            'nspace':unit['prefix']
            }]
    b_dict[10] = [{
            'table':unit['tag']
            }]
    tbl_offset = tbl_offset + vindex
    return b_dict

import os

def write_hu(dc, pack):
    res = tpl.load_tpl(open('_hu.java.tpl').read(), dc)
    if pack != '':
        res = res.replace('%java_package%', java_package+'.'+pack)
    else:
        res = res.replace('%java_package%', java_package)
    for key in import_map:
        if res.find(key) != -1:
            res_g = res.split('//auto import')
            res = res_g[0] + '//auto import\nimport ' + import_map[key] + ';' + res_g[1]
    for i in range(0,len(sw)):
        pattern = re.compile('\[\[\?'+str(i)+'([^\d](.|\n)*?)\]\]')
        got = pattern.findall(res)
        if sw[i]:
            res = re.sub('\[\[\?'+str(i)+'([^\d](.|\n)*?)\]\]', got[0][0], res)
        else:
            res = re.sub('\[\[\?'+str(i)+'([^\d](.|\n)*?)\]\]', '', res)
    if not os.path.exists(code_dir + '/' + pack):
        os.makedirs(code_dir+'/'+pack)
    open(code_dir+'/'+pack+'/_Hu'+java_var(unit['hu'], True)+'.java', 'w').write(res)
    ins_conf.append(pack+'._Hu'+java_var(unit['hu'], True))
    
def write_model(dc):
    if dc != None:
        res = tpl.load_tpl(open('_model.java.tpl').read(), dc)
        for key in import_map:
            if res.find(key) != -1:
                res_g = res.split('//auto import')
                res = res_g[0] + '//auto import\nimport ' + import_map[key] + ';' + res_g[1]
        try:
            fr = open(model_dir+'Model'+java_var(unit['model'], True)+'.java')
            old_body = fr.read()
            fr.close()
            pattern = re.compile('(//udef-[a-z0-9A-Z]+)')
            udefs = pattern.findall(res)
            udef_set = []
            for udef in udefs:
                for udef2 in udef_set:
                    if udef == udef2:
                        raise 'same udef value' + udef + " in " + unit['model']
                udef_set.append(udef)
                rb = res.split(udef)
                rb_append = ''
                st = old_body.find(udef)
                if st != -1:
                    sr = old_body[st:]
                    ed = sr.find('//uend')
                    if ed != -1:
                        rb_append = sr[:ed]
                rb_append = udef + rb_append.replace(udef, '')
                res = rb[0] + rb_append + '//uend' + rb[1]
        except:
            pass
        open(model_dir+'Model'+java_var(unit['model'], True)+'.java', 'w').write(res)

def clean_gen():
    lf = os.listdir(code_dir)
    for fl in lf:
        sdir = code_dir+'/'+fl
        if os.path.isdir(sdir):
            fe = os.listdir(sdir)
            for fee in fe:
                if fee != '_Hv.java':
                    os.remove(sdir+'/'+fee)
            os.rmdir(sdir)

if __name__ == '__main__':
    clean_gen()
    ins_conf = []
    for f in sql_files:
        print '#################'
        print 'in file ' + f
        units = analyse.analyse_file(db_dir + f)
        pack = ''
        if f.find('/') != -1 and fsrc != 'pangea':
            pack = f.split('/')[0]
        for unit in units:
            dc = map_hu(unit)
            print '-pack-:'+pack
            write_hu(dc, pack)
            if model_dir != None:
                dc = map_model(unit)
                write_model(dc)
    f = open(code_dir + '_Hconf.java', 'r')
    e = f.read()
    f.close()
    x = e.split('//--auto load')
    y = x[1].split('//--end')
    e = x[0] + '//--auto load\n'
    imp = None
    for key in ins_conf:
        if -1 != key.find('.'):
            unpack = key.split('.')
            pack = unpack[0]
            key = unpack[1]
            if imp == None:
                imp = set()
            imp.add(pack)
        e = e + '        RegDbUnit(new ' + key + '());\n'
    e = e + '        //--end' + y[1]
    if imp != None:
        x = e.split('//--auto imp')
        y = x[1].split('//--end imp')
        e = x[0] + '//--auto imp\n'
        for impe in imp:
            e = e + 'import '+java_package+'.'+impe+'.*;\n'
        e = e + '//--end imp' + y[1]
    f = open(code_dir + '_Hconf.java', 'w')
    f.write(e)
    f.close()
