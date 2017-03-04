import ChatApp.*;          // The package containing our stubs
import org.omg.CosNaming.*; // HelloClient will use the naming service.
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;     // All CORBA applications need these classes.
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Scanner;
import java.util.Arrays;


class ChatCallbackImpl extends ChatCallbackPOA
{
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public void callback(String notification)
    {
        System.out.println(notification);
    }
}

public class ChatClient
{
    static Chat chatImpl;

    public static void main(String args[])
    {
	try {
	    // create and initialize the ORB
	    ORB orb = ORB.init(args, null);

	    // create servant (impl) and register it with the ORB
	    ChatCallbackImpl chatCallbackImpl = new ChatCallbackImpl();
	    chatCallbackImpl.setORB(orb);

	    // get reference to RootPOA and activate the POAManager
	    POA rootpoa =
		POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

	    // get the root naming context
	    org.omg.CORBA.Object objRef =
		orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	    // resolve the object reference in naming
	    String name = "Chat";
	    chatImpl = ChatHelper.narrow(ncRef.resolve_str(name));

	    // obtain callback reference for registration w/ server
	    org.omg.CORBA.Object ref =
		rootpoa.servant_to_reference(chatCallbackImpl);
	    ChatCallback cref = ChatCallbackHelper.narrow(ref);

	    // Application code goes below

      Scanner scan = new Scanner(System.in);
      String msg = "";
      String command = "";
      String message = "";
      for (;;){
        msg = scan.nextLine();
        try{
          command = msg.split(" ")[0];

          if (command.equals("join")){
            message = msg.split(" ", 2)[1];
            chatImpl.join(cref, message);
          }else if (command.equals("list")){
            chatImpl.list(cref);
          }else if (command.equals("post")){
            message = msg.split(" ", 2)[1];
            chatImpl.post(cref, message);
          }else if (command.equals("leave")){
            chatImpl.leave(cref, false);
          }else if (command.equals("game")){
            message = msg.split(" ", 2)[1];
            chatImpl.game(cref, message);
          }else if (command.equals("move")){
            message = msg.split(" ", 2)[1];
            chatImpl.move(cref, message);
          }else if (command.equals("quit")){
              chatImpl.leave(cref, true);
              System.exit(0);
          }else{
            System.out.println("bad command!");
          }
        }catch(Exception e){
          System.out.println("Bad command format");
        }
      }

	} catch(Exception e){
	    System.out.println("ERROR : " + e);
	    e.printStackTrace(System.out);
	}
    }
}
