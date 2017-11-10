import re

def load_tpl(src, trans_dict):
    model_body = {}
    model_body['src'] = src
    model_body['elem'] = []
    pattern = re.compile('\\{\\{#(\\d+)([^\\d](.|\n)*?)\\}\\}')
    res = pattern.findall(src)
    for r_unit in res:
        srt = r_unit[1]
        rbt = ''
        dr = trans_dict[int(r_unit[0])]
        fst = True
        for de in dr:
            if fst:
                fst = False
            else:
                rbt = rbt + '\n'
            n_srt = srt
            for key in de:
                if srt == 'table':
                    print r_unit
                n_srt = n_srt.replace('$'+key, str(de[key]))
                if srt == 'table':
                    print '%%%%%%%%'
                    print de[key]
                    print n_srt
            rbt = rbt+n_srt
        if rbt.find('$,'):
            sub = rbt.split('$,')
            rbt = ''
            fst = True
            for v in range(0, len(sub) - 1):
                if fst:
                    fst = False
                else:
                    rbt = rbt + ','
                rbt = rbt + sub[v]
            rbt = rbt + sub[len(sub)-1]
        src= re.sub('\\{\\{#'+r_unit[0]+'[^\\d](.|\n)*?\\}\\}', rbt, src)
    return src
