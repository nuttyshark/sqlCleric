package target.model;

import com.clearso.hermes.HermesCreateBuilder;
import com.clearso.hermes.HermesDbHolder;
import com.clearso.hermes.HermesRetrieveBuilder;
import com.clearso.hermes.HermesUpdateBuilder;
import com.clearso.hermes.HermesDeleteBuilder;

import target.hermes.gen._Hv;
import target.hermes.gen._Hu{{#2$unit}};

//auto import

//udef-import

@SuppressWarnings("unchecked")
public class Model{{#0$model}} extends _Model {

	public Model{{#0$model}}(){
		super();
	}
	
	public Model{{#0$model}}(HermesDbHolder dh){
		super(dh);
	}

    //udef-func

	public {{#5$ret_type}} Create(
{{#1                         $type $col_name$,}}
                       ){
		_Hu{{#2$unit}} hu = new _Hu{{#2$unit}}();
{{#3        hu.$var = $trans;}}
		hu.{{#4$ret_col}} = ({{#5$ret_type}})new HermesCreateBuilder(dh)
				.Load(hu)
                .Ret(_Hu{{#2$unit}}.c{{#4$ret_col}})
				.Exec(rs -> {
					rs.ret = rs.Get{{#5$ret_type}}();
					return false;
				});
        //udef-aftercreate
        return hu.{{#4$ret_col}};
	}

	public HermesArray<HermesMap> Search(HermesMap cond, HermesMap lo){
		HermesRetrieveBuilder hrb = new HermesRetrieveBuilder(dh);
		hrb.Src(_Hu{{#2$unit}}._Offset)
           .Col("count(^_^) OVER()", _Hu{{#2$unit}}.c{{#4$ret_col}})
{{#7       .Col(_Hu$unit.c$col)}};
		if(lo != null){
			hrb.L(lo.getInteger("num"))
				.S(lo.getInteger("page") * lo.getInteger("num"));
		}
        //udef-searchext
		HermesArray<HermesMap> rt = (HermesArray<HermesMap>)hrb.Exec(rs -> {
            HermesArray<HermesMap> res = rs.ret(HermesArray.class, r ->{
                return new HermesArray<HermesMap>();
            });
			HermesMap rm = new HermesMap();
            rm.put("_cnt", rs.GetInteger());
{{#8            rm.put("$cname", _Hu$unit.Load(rs, _Hu$unit.c$col));}}
            //udef-searchres
            res.Push(rm);
			return false;
		});
        if (rt == null){
            rt = new HermesArray<HermesMap>();
        }
        return rt;
	}
    
	public Object Modify({{#5$ret_type}} {{#10$ret_var}}, HermesMap jr){
        HermesMap cond = new HermesMap();
        cond.put("{{#10$ret_var}}",{{#10$ret_var}});
        return Modify(cond, jr);
    }
        
    public Object Modify(HermesMap cond, HermesMap jr){
		_Hu{{#2$unit}} hs = new _Hu{{#2$unit}}();
		hs.Load(jr, 
{{#9			/* key -> $key */_Hu$unit.c$col$,}}
        );
        HermesUpdateBuilder hub = new HermesUpdateBuilder(dh);
        if(cond.containsKey("{{#10$ret_var}}")){
			hub.Conde(_Hv.V(cond.get{{#5$ret_type}}("{{#10$ret_var}}")), _Hu{{#2$unit}}.c{{#4$ret_col}});
        }
        //udef-modify
		return hub
			.Src(hs)
			.Ret(_Hu{{#2$unit}}.c{{#4$ret_col}})
			.Exec(rs -> {
                //udef-modretb
				rs.ret = rs.Get{{#5$ret_type}}();
                //udef-modrete
				return false;
			});
	}

	public HermesMap Get({{#5$ret_type}} {{#10$ret_var}}){
        HermesMap rt = Get(HermesMap.Puts("{{#10$ret_var}}", {{#10$ret_var}}));
        return rt;
    }
        
    public HermesMap Get(HermesMap cond){
		HermesRetrieveBuilder hrb = new HermesRetrieveBuilder(dh);
		hrb.Src(_Hu{{#2$unit}}._Offset)
{{#11       .Col(_Hu$unit.c$col)}}
		   .L(1)
           .S(0);
        if(cond.containsKey("{{#10$ret_var}}")){
			hrb.Conde(_Hv.V(cond.get{{#5$ret_type}}("{{#10$ret_var}}")), _Hu{{#2$unit}}.c{{#4$ret_col}});
        }
        //udef-get2
		HermesMap rt = (HermesMap)hrb.Exec(rs -> {
            HermesMap rm = rs.ret(HermesMap.class, r->{
                return new HermesMap();
            });
{{#12            rm.put("$cname", _Hu$unit.Load(rs, _Hu$unit.c$col));}}
            //udef-getres2
			return false;
		});
        //udef-got
        return rt;
    }
        
	public void Remove(HermesMap cond){
		HermesDeleteBuilder hdb = new HermesDeleteBuilder(dh);
		hdb.Src(_Hu{{#2$unit}}._Offset);
        if(cond.containsKey("{{#10$ret_var}}")){
            hdb.Conde(_Hv.V(cond.get{{#5$ret_type}}("{{#10$ret_var}}")), _Hu{{#2$unit}}.c{{#4$ret_col}});
        }
        //udef-remove
		hdb.Exec();
        //udef-aftremove
	}
	
}
