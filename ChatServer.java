import ChatApp.*;          // The package containing our stubs.
import org.omg.CosNaming.*; // HelloServer will use the naming service.
import org.omg.CosNaming.NamingContextPackage.*; // ..for exceptions.
import org.omg.CORBA.*;     // All CORBA applications need these classes.
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.*;

class ChatImpl extends ChatPOA
{
    ArrayList<String> userList = new ArrayList<String>();
    ArrayList<ChatCallback> connList = new ArrayList<ChatCallback>();

    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public String say(ChatCallback callobj, String msg)
    {
        callobj.callback(msg);
        System.out.println(msg);
        return ("         ....Goodbye!\n");
    }


    public String join(ChatCallback callobj, String msg)
    {
        int index;

        if (!userList.contains(msg)){
          if (connList.contains(callobj)){
            index = connList.indexOf(callobj);
            connList.remove(index);
            userList.remove(index);
          }
          userList.add(msg);
          connList.add(callobj);

          callobj.callback("Welcome "+msg);
          return "Welcome "+msg;
        }else{
          callobj.callback("The username "+msg+" is not available.");
          return "The username "+msg+" is not available.";
        }
    }

    public String list(ChatCallback callobj){
      String rt = "";

      for(int i = 0; i < userList.size(); i++){
        rt += userList.get(i)+"\n";
      }
      callobj.callback(rt);
      return rt;
    }

    public String post(ChatCallback callobj, String msg){
      String username;
      int index = connList.indexOf(callobj);
      if (index == -1){
        callobj.callback("You are not logged in!");
        return "You are not logged in!";
      }else{
        username = userList.get(index);
        for (int i = 0; i < connList.size(); i++){
          connList.get(i).callback(username+": "+msg);
        }
        return "";
      }
    }

    public String leave(ChatCallback callobj){
      int index;
      if (connList.contains(callobj)){
        index = connList.indexOf(callobj);
        connList.remove(index);
        userList.remove(index);
        callobj.callback("Bye!");
        return "Bye!";
      }else{
        callobj.callback("You are not loged in!");
        return "You are not logged in!";
      }

    }
}

public class ChatServer
{
    public static void main(String args[])
    {
	try {
	    // create and initialize the ORB
	    ORB orb = ORB.init(args, null);

	    // create servant (impl) and register it with the ORB
	    ChatImpl chatImpl = new ChatImpl();
	    chatImpl.setORB(orb);

	    // get reference to rootpoa & activate the POAManager
	    POA rootpoa =
		POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	    rootpoa.the_POAManager().activate();

	    // get the root naming context
	    org.omg.CORBA.Object objRef =
		           orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	    // obtain object reference from the servant (impl)
	    org.omg.CORBA.Object ref =
		rootpoa.servant_to_reference(chatImpl);
	    Chat cref = ChatHelper.narrow(ref);

	    // bind the object reference in naming
	    String name = "Chat";
	    NameComponent path[] = ncRef.to_name(name);
	    ncRef.rebind(path, cref);

	    // Application code goes below
	    System.out.println("ChatServer ready and waiting ...");

	    // wait for invocations from clients
	    orb.run();
	}

	catch(Exception e) {
	    System.err.println("ERROR : " + e);
	    e.printStackTrace(System.out);
	}

	System.out.println("ChatServer Exiting ...");
    }

}
