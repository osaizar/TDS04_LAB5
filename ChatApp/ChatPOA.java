package ChatApp;


/**
* ChatApp/ChatPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Chat.idl
* Thursday, March 2, 2017 4:25:37 PM CET
*/

public abstract class ChatPOA extends org.omg.PortableServer.Servant
 implements ChatApp.ChatOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("join", new java.lang.Integer (0));
    _methods.put ("list", new java.lang.Integer (1));
    _methods.put ("post", new java.lang.Integer (2));
    _methods.put ("leave", new java.lang.Integer (3));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // ChatApp/Chat/join
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String name = in.read_string ();
         String $result = null;
         $result = this.join (objref, name);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // ChatApp/Chat/list
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String $result = null;
         $result = this.list (objref);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // ChatApp/Chat/post
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String msg = in.read_string ();
         String $result = null;
         $result = this.post (objref, msg);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // ChatApp/Chat/leave
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String $result = null;
         $result = this.leave (objref);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ChatApp/Chat:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Chat _this() 
  {
    return ChatHelper.narrow(
    super._this_object());
  }

  public Chat _this(org.omg.CORBA.ORB orb) 
  {
    return ChatHelper.narrow(
    super._this_object(orb));
  }


} // class ChatPOA