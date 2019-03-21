package %java_package%;

import com.clearso.hermes.core.HermesDbUnit;
import com.clearso.hermes.core.HermesException;
import com.clearso.hermes.core.HermesSqlVal;

//auto import

public class Hu{{#0$model}} extends HermesDbUnit {

{{#1    public $type $var = null;}}
	
    public static final int _Offset = {{#2$offset}};
	
{{#3    public static final int _$var = _Offset+$vindex;}}

{{#4    public static final Integer c$var = _$var;}}
	
    public static final int _colNum = {{#5$cnum}};
    protected static String[] _colName = new String[]{
{{#6                                                   "$col_name"$,}}

                                                        };
	
    public Hu{{#0$model}}(){
        super("{{#7$nspace}}.");
    }

    @Override
    public HermesDbUnit clone(){
        return new Hu{{#0$model}}();
    }

    @Override
    public Hu{{#0$model}} Clear() {
{{#8        $var = null;}}
        return this;
    }
	
    @Override
    public int ColNum() {
        return _colNum;
    }

    @Override
    public String ColName(int index, boolean prefix) {
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

	@Override
	public String Tag() {
		return "{{#10$table}}";
	}

	@Override
	public int HasCol(String col) {
		for(int i=0; i<_colName.length; i++){
			if(_colName[i].equals(col)){
				return i+_Offset;
			}
		}	
		return -1;
	}

	@Override
	public int Offset() {
		return _Offset;
	}

	@Override
	public String Var(int index) {
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
