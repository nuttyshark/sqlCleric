package %java_package%;

import com.clearso.hermes.HermesDbUnit;
import com.clearso.hermes.HermesException;
import com.clearso.hermes.HermesSqlVal;
import com.clearso.hermes.HermesResultSet;

//auto import

public class _Hu{{#0$model}} extends HermesDbUnit {

{{#1    public $type $var = null;}}
	
    public static final int _Offset = {{#2$offset}};
	
{{#3    public static final int _$var = _Offset+$vindex;}}

{{#4    public static final Integer c$var = _$var;}}
	
    public static final int _colNum = {{#5$cnum}};
    protected static String[] _colName = new String[]{
{{#6                                                   "$col_name"$,}}

                                                        };
	
    public _Hu{{#0$model}}(){
        super("{{#7$nspace}}.");
    }

    @Override
    public _Hu{{#0$model}} Clear() {
        // TODO Auto-generated method stub
{{#8        $var = null;}}
        return this;
    }
	
    @Override
    public int ColNum() {
        // TODO Auto-generated method stub
        return _colNum;
    }

    @Override
    public String ColName(int index, boolean prefix) {
        // TODO Auto-generated method stub
        if(ValidCol(index)){
            index -= _Offset;
            if(prefix){
                return TableName()+"."+_colName[index];
            }else{
                return _colName[index];
            }
        }else{
            return null;
        }
    }

    [[?0@SuppressWarnings("unchecked")]]
    @Override
    public HermesDbUnit Col(int index, Object val){
        // TODO Auto-generated method stub
        boolean success = false;
        if(!ValidCol(index)){
            throw new HermesException("Invalid col index");
        }
        switch(index){
{{#9            case _$var:
                if(val instanceof $tyg){
                    $var = ($type)val;
                    success = true;
                }
            break;}}
		default:
			break;
		}
		if(!success){
			throw new HermesException(_colName[index] + " received illegal " + val.getClass().getName());
		}
		return this;
	}

    public void Load(HermesMap jr, Integer...cols){
        for(Integer col:cols){
            if(!ValidCol(col)){
                throw new HermesException("Invalid col index");
            }
            
            switch(col){
{{#12            case _$var:
                if(jr.containsKey("$col")){
                   $var = $stjr.get$type"$col")$ed;
                }
                break;}}
            default:
                break;
            }
        }
    }

    public static Object Load(HermesResultSet rs, int col){
        Object rt = null;
        switch(col){
{{#13        case _$var:
            rt = $strs.Get$type)$ed;
            break;}}
        default:
            break;
        }
        return rt;
    }

	@Override
	public String Tag() {
		// TODO Auto-generated method stub
		return "{{#10$table}}";
	}

	@Override
	public int HasCol(String col) {
		// TODO Auto-generated method stub
		for(int i=0; i<_colName.length; i++){
			if(_colName[i].equals(col)){
				return i+_Offset;
			}
		}	
		return -1;
	}

	@Override
	public int Offset() {
		// TODO Auto-generated method stub
		return _Offset;
	}

	@Override
	public String Var(int index) {
		// TODO Auto-generated method stub
		Object rt = null;
		if(!ValidCol(index)){
			throw new HermesException("Invalid col index");
		}
		switch(index){
{{#11		    case _$var:
			     rt = $var;
			     break;}}
            default:
			     break;
		}
        if(rt == null){
            return null;              
        }
		return new HermesSqlVal(rt).toString();
	}

}
