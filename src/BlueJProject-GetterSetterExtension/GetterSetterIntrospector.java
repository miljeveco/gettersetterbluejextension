import java.lang.reflect.*;
import bluej.extensions2.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


/**
 * GetterSetterIntrospector  use API Java-Reflect
 * 
 * @author (Milton Jes&uacute;s Vera Contreras - miltonjesusvc@ufps.edu.co) 
 * @version 0.0000000000000001 --> Math.sin(Math.PI-Double.MIN_VALUE) --> :)
 */
public class GetterSetterIntrospector
{
    private BObject theBObject;
    private int theBObjectPosition;
    private int totalObjects;
    private BClass theBClass;
    private Class theClass;
    private Method [] theGetMethods;
    private Method [] theSetMethods;
    private String [] propertiesWithGetSet;

    private final String wrappers   = "Character,Byte     ,Short    ,Integer  ,Long     ,Float    ,Double   ,String   ,Boolean";
    private final String primitives = "char     ,byte     ,short    ,int      ,long     ,float    ,double   ,String   ,boolean";   

    /**
     * Default constructor
     * @param theBClass An object BClass with the Class
     */
    public GetterSetterIntrospector(BClass theBClass) throws Exception
    {
        setTheBClass(theBClass);
    }//end constructor

    /**
     * Default constructor
     * @param theBObject An object BObject with the Object
     */
    public GetterSetterIntrospector(BObject theBObject) throws Exception
    {
        this.setTheBObject(theBObject);
    }//end constructor

    /**
     * Use java.lang.Class 
     * View theClass and get methods GET/SET
     */
    private void introspectForGetterSetter()
    {
        Method [] methods = theClass.getMethods();
        ArrayList sets = new ArrayList();
        ArrayList gets = new ArrayList();

        for(int i=0; i<methods .length; i++){
            if(isGetMethod(methods[i]))
                gets.add(methods[i]);
            else
            if(isSetMethod(methods[i]))
                sets.add(methods[i]);
        }//end for

        this.theGetMethods = new Method[gets.size()];
        for(int i=0; i<gets.size();i++) this.theGetMethods[i] = (Method) gets.get(i);

        this.theSetMethods = new Method[sets.size()];
        for(int i=0; i<sets.size();i++) this.theSetMethods[i] = (Method) sets.get(i);     

    }//end introspectForGetterSetter

    /**
     * A method is GET only if starts with GET and have return
     */
    private boolean isGetMethod(Method theMethod)
    {
        boolean startsWithGet = theMethod.getName().startsWith("get") && theMethod.getName().length() > 3;
        boolean notHasParameters = theMethod.getParameterTypes().length==0;
        boolean hasReturn = ! theMethod.getReturnType().getName().equals("void") && 
            (theMethod.getReturnType().isPrimitive() || 
                    //theMethod.getReturnType().isArray() ||
                theMethod.getReturnType().getName().equals("java.lang.String"));
        return startsWithGet && notHasParameters && hasReturn;
    }//end isGetMethod

    /**
     * A method is SET only if starts with SET and have not return
     */
    private boolean isSetMethod(Method theMethod)
    {
        boolean startsWithSet = theMethod.getName().startsWith("set") && theMethod.getName().length() > 3;
        boolean hasOnlyOneParameterPrimitiveOrString = theMethod.getParameterTypes().length==1 &&
            (theMethod.getParameterTypes()[0].isPrimitive() || 
                    //theMethod.getParameterTypes()[0].isArray() || 
                theMethod.getParameterTypes()[0].getName().equals("java.lang.String"));
        boolean noReturn = theMethod.getReturnType().getName().equals("void");
        return startsWithSet && hasOnlyOneParameterPrimitiveOrString  && noReturn;
    }//end isSetMethod

    /**Return an String [] with all properties with GET method*/
    public String [] getPropertiesWithGet()
    {
        return getProperties(theGetMethods);
    }//end getPropertiesWithGet

    /**Return an String [] with all properties with SET method*/
    public String [] getPropertiesWithSet()
    {
        return getProperties(theSetMethods);
    }//end getPropertiesWithSet

    /**Return an String [] with name properties of methods*/
    private String [] getProperties(Method [] methods)
    {
        ArrayList properties = new ArrayList();
        for(int i=0; i<methods.length;i++)
            properties.add(methods[i].getName().substring(3));
        return (String []) properties.toArray();        
    }//end getProperties

    /**Return an String [] with all properties without GET method*/
    public String [] getPropertiesWithoutGet() throws Exception
    {
        Field [] fields = theClass.getDeclaredFields();
        StringBuffer properties = new StringBuffer();

        for(int i=0; i<fields.length;i++)
        {
            String propertie = fields[i].getName().substring(0,1).toUpperCase() + fields[i].getName().substring(1);
            if(getBMethodGet(propertie) == null)
                properties.append(fields[i].getName()+",");
        }//end for fields

        if(properties.length() > 0)return properties.substring(0, properties.length()-1).split(",");
        else return new String[0];
    }//end getPropertiesWithoutGet

    /**Return an String [] with all properties without SET method*/
    public String [] getPropertiesWithoutSet() throws Exception
    {
        Field [] fields = theClass.getDeclaredFields();
        StringBuffer properties = new StringBuffer();

        for(int i=0; i<fields.length;i++)
        {
            String propertie = fields[i].getName().substring(0,1).toUpperCase() + fields[i].getName().substring(1);
            if(getBMethodSet(propertie) == null)
                properties.append(fields[i].getName()+",");
        }//end for fields

        if(properties.length() > 0)return properties.substring(0, properties.length()-1).split(",");
        else return new String[0];
    }//end getPropertiesWithoutSet    

    /**Wrapper BMethod Bluej "set"*/
    public BMethod getBMethodSet(String propertie) throws Exception
    {
        BMethod method = null;
        for(int i=0; i<theSetMethods.length;i++){
            if(propertie.equals(theSetMethods[i].getName().substring(3))){
                method = theBClass.getMethod("set"+propertie, theSetMethods[i].getParameterTypes());
                break;
            }//end if
        }//end for
        return method;
    }//end

    /**Wrapper BMethod Bluej "get"*/
    public BMethod getBMethodGet(String propertie) throws Exception
    {
        BMethod method = null;
        for(int i=0; i<theGetMethods.length;i++){
            if(propertie.equals(theGetMethods[i].getName().substring(3))){
                method = theBClass.getMethod("get"+propertie, new Class[]{});
                break;
            }//end if
        }//end for
        return method;
    }//end    

    /**Return an String [] with all properties with GET/SET method*/
    public String [] getPropertiesWithGetSet(){
        if(this.propertiesWithGetSet==null){
            ArrayList set = new ArrayList();
            ArrayList getSet = new ArrayList();

            for(int i=0; i<this.theSetMethods.length;i++)
                set.add(this.theSetMethods[i].getName().substring(3));

            for(int i=0; i<this.theGetMethods.length;i++)
                if(set.contains(this.theGetMethods[i].getName().substring(3)))
                    getSet.add(this.theGetMethods[i].getName().substring(3));

            this.propertiesWithGetSet = new String[getSet.size()];
            for(int i=0; i<getSet.size();i++)
                this.propertiesWithGetSet[i] = (String)getSet.get(i);
        }//end if
        return this.propertiesWithGetSet;
    }//end method getPropertiesWithGetSet

    /**Getter BObject*/
    public BObject getTheBObject() throws Exception{
        BObject obj=null;
        if(theBObjectIsArray()) {
            obj = (BObject)BArray.getValue(theBObject, theBObjectPosition);
        }
        else obj = this.theBObject;
        return obj;
    }//end method getTheBObject

    /**Setter BObject*/
    public void setTheBObject(BObject theBObject) throws Exception
    {
        this.theBObject = theBObject;
        this.setTheBClass();
        this.introspectForGetterSetter();
    }//end method getTheBObject

    /**Getter BClass*/
    private void setTheBClass() throws Exception
    {
        BObject obj=null;
        if(theBObjectIsArray()){
            this.calculateLengthArray();
            obj = (BObject)BArray.getValue(theBObject, 0);
        }//end is array
        else{
            obj = this.theBObject;
        }//end not is array
        this.theBClass = obj.getBClass();
        this.theClass = this.theBClass.getJavaClass();
    }//end setTheBClass

    /**Setter BClass*/
    private void setTheBClass(BClass theBClass) throws Exception
    {
        this.theBClass = theBClass;
        this.theClass = this.theBClass.getJavaClass();
        this.introspectForGetterSetter();
    }//end setTheBClass

    /**Return true if BObject is an array*/
    public boolean theBObjectIsArray()
    {
        return  this.theBObject.toString().indexOf("[")!=-1;
    }//end isArray

    /**Return the current position on array if BObject is an array*/
    public int getTheBObjectPosition(){return this.theBObjectPosition;}

    /**Change to i the current position on array if BObject is an array*/
    public void setTheBObjectPosition(int i){this.theBObjectPosition=i;}

    /**Change to 0 the current position on array if BObject is an array*/
    public void firstBObject(){this.theBObjectPosition=0;}

    /**Change to last position the current position on array if BObject is an array*/
    public void lastBObject(){this.theBObjectPosition=this.totalObjects-1;}

    /**Change to next position the current position on array if BObject is an array*/
    public void nextBObject(){this.theBObjectPosition = (this.theBObjectPosition+1)%this.totalObjects;}

    /**Change to previous position the current position on array if BObject is an array*/
    public void previousBObject(){this.theBObjectPosition = (this.theBObjectPosition -1+this.totalObjects)%this.totalObjects;}

    /**Return array length if BObject is an array*/
    public int getTotalObjects(){return this.totalObjects;}

    /**Calculate array length if BObject is an array*/
    private void calculateLengthArray()
    {
        this.totalObjects = 0;
        boolean error = false;
        while(! error){
            try{
                BArray.getValue(theBObject, this.totalObjects);
                this.totalObjects++;
            }catch(Exception e){error = true;}        
        }//end while
    }//end calculateLengthArray
    
    /**Getter bClass*/
    public BClass getTheBClass(){
        return this.theBClass;
    }//end method getTheBClass

    /**Getter Class*/
    public Class getTheClass(){
        return this.theClass;
    }//end method getTheClass

    /**Return all GET methods*/
    public Method[] getTheGetMethods(){
        return this.theGetMethods;
    }//end method getTheGetMethods

    /**Return all SET methods*/
    public Method[] getTheSetMethods(){
        return this.theSetMethods;
    }//end method getTheSetMethods

    /**Invoke Getter Method and return the value of propertie*/
    public String invokeGetter(String propertie)
    {
        String result;
        BObject obj=null;
        try{
            BMethod method = this.getBMethodGet(propertie);
            obj = getTheBObject();
            if(theBObjectIsArray()) obj.addToBench(obj.getInstanceName());
            result = method.invoke(obj, new Object[]{}).toString();
        }//end try
        catch(Exception e){
            result = e.getMessage();
            java.io.StringWriter errors = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(errors));
            System.err.println("GetterSetter Extension error: "+ e.getMessage() + "\n" + errors.toString());
        }
        finally{
         try{if(theBObjectIsArray()) obj.removeFromBench();}catch(Exception e1){}
        }
        return result;
    }//end invokeGetter

    /**Invoke Setter Method and change the value of propertie*/
    public boolean invokeSetter(String propertie, String textValue)
    {
        boolean rta = true;
        BObject obj=null;
        try{
            BMethod method = this.getBMethodSet(propertie);
            obj = getTheBObject();
            String typeParameter = method.getParameterTypes()[0].getSimpleName();
            int type = primitives.indexOf(typeParameter)/10;
            type = type < 0 ? wrappers.indexOf(typeParameter)/10 : type;
            Object value=null;        
            switch(type){
                case 0: value = new Character(textValue.charAt(0));break;
                case 1: value = Byte.valueOf(textValue);break;
                case 2: value = Short.valueOf(textValue);break;
                case 3: value = Integer.valueOf(textValue);break;
                case 4: value = Long.valueOf(textValue);break;
                //Temporal hack. How to exception of float type?
                case 5: value = Integer.valueOf(((int)Double.valueOf(textValue).floatValue()));break;
                case 6: value = Double.valueOf(textValue);break;
                case 7: value = textValue;break;
                case 8: value = new Boolean(textValue);break;
                default: rta = false;
            }//end switch
            if(theBObjectIsArray()) obj.addToBench(obj.getInstanceName());
            method.invoke(obj, new Object[]{value});
        }//end try
        catch(Exception e){
            rta = e.getMessage().indexOf("result==null")!=-1;
            java.io.StringWriter errors = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(errors));
            System.err.println("GetterSetter Extension error: "+ e.getMessage() + "\n" + errors.toString());
        }//fin catch
        finally{
          try{if(theBObjectIsArray()) obj.removeFromBench();}catch(Exception e1){}
        }
        return rta;
    }//end invokeSetter    

}//fin class GetterSetterIntrospector