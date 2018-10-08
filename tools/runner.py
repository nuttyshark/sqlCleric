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
    'HermesArray':'com.clearso.hermes.types.HermesArray',
    'HermesRange':'com.clearso.hermes.types.HermesRange',
    'HermesMap':'com.clearso.hermes.types.HermesMap',
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
        b_dict[9].append({
            'var':java_var(col['name'], True),
            'tyg':col['type']['tyg'],
            'type':col['type']['gen']
        })
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
    res = tpl.load_tpl(open(tpl_dir+'/_hu.java.tpl').read(), dc)
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

def clean_gen():
    print 'path:' + os.getcwd()
    lf = os.listdir(code_dir)
    for fl in lf:
        sdir = code_dir+'/'+fl
        if os.path.isdir(sdir):
            fe = os.listdir(sdir)
            for fee in fe:
                if fee != '_Hv.java':
                    os.remove(sdir+'/'+fee)
            os.rmdir(sdir)

import sys

if __name__ == '__main__':
    if len(sys.argv) > 1:
        # replace param
        if len(sys.argv) < 5:
            print 'generated failed'
        else:
            db_dir = sys.argv[1]
            code_dir = sys.argv[2]
            java_package = sys.argv[3]
            sql_files = []
            for i in range(4, len(sys.argv)):
                sql_files.append(sys.argv[i])
    print db_dir
    print code_dir
    print java_package
    print sql_files
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
    f = open(code_dir + '/_Hconf.java', 'r')
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
            if len(impe)<2:
                e = e + 'import '+java_package+'.*;\n'
            else:
                e = e + 'import '+java_package+'.'+impe+'.*;\n'
        e = e + '//--end imp' + y[1]
    f = open(code_dir + '_Hconf.java', 'w')
    f.write(e)
    f.close()
