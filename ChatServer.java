import ChatApp.*;          // The package containing our stubs.
import org.omg.CosNaming.*; // HelloServer will use the naming service.
import org.omg.CosNaming.NamingContextPackage.*; // ..for exceptions.
import org.omg.CORBA.*;     // All CORBA applications need these classes.
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.*;


class ChatImpl extends ChatPOA
{
    private ArrayList<String> userList = new ArrayList<String>();
    private ArrayList<ChatCallback> connList = new ArrayList<ChatCallback>();
    private ArrayList<Integer> playerList = new ArrayList<Integer>(); // 0 not playing 1 -> x 2 -> o
    private Game game = null;

    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }


    public String join(ChatCallback callobj, String msg)
    {
        int index;

        if (!userList.contains(msg)){
          if (connList.contains(callobj)){
            callobj.callback("You are already logged in!");
            return "";
          }
          userList.add(msg);
          connList.add(callobj);
          playerList.add(Integer.valueOf(0)); //not a player

          callobj.callback("Welcome "+msg);
          index = connList.indexOf(callobj);

          for (int i = 0; i < connList.size(); i++){
            if(index != i){
              connList.get(i).callback(msg+" has joined!");
            }
          }

          return "";
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


    public String game(ChatCallback callobj, String msg){
      int index = 0;
      int value = 0;
      int team  = 0;

      if (!connList.contains(callobj)){
        callobj.callback("You are not logged in!");
        return "";
      }
      if (!msg.equals("x") && !msg.equals("o")){
        callobj.callback("The color is not correct (x or o)");
        return "";
      }
      if (game == null){ // new game
        game = new Game();
        callobj.callback("A new 5 on a row game has started!");
      }
      index = connList.indexOf(callobj);

      if (msg.equals("x")){
        team = 1;
      }else{
        team = 2;
      }

      playerList.set(index, Integer.valueOf(team));
      callobj.callback("You joined "+msg+" team");
      callobj.callback(game.getGameBoard());
      return "";
    }


    public String move(ChatCallback callobj, String msg){
      int index, team, row, col, winner;
      String rt;
      String[] split;

      if (!connList.contains(callobj)){
        callobj.callback("You are not logged in!");
        return "";
      }
      if (game == null){
        callobj.callback("There is no game running");
        return "";
      }
      index = connList.indexOf(callobj);
      team = playerList.get(index);
      if(team == 0){
        callobj.callback("You are not on a team!");
        return "";
      }
      try{
        split = msg.split(" ");
        row = Integer.parseInt(split[0]);
        col = Integer.parseInt(split[1]);
        rt = game.makeMove(team, row, col);
        if (!rt.equals("correct")){
          callobj.callback(rt);
          callobj.callback(game.getGameBoard());
          return "";
        }
        winner = game.getWinner();
        for (int i = 0; i < connList.size(); i++){
          if (playerList.get(i) != 0){
            connList.get(i).callback(userList.get(index)+" has made a move");
            connList.get(i).callback(game.getGameBoard());
            if (winner != 0){// WTF
              connList.get(i).callback("Team "+winner+" wins the game!");
            }
          }
        }
        if (winner != 0){
          game = null;
        }
        return "";
      }catch(Exception e){mov
        callobj.callback("Bad input");
      }
      return "";
    }


    public String leave(ChatCallback callobj, boolean silent){
      int index;
      String name;
      if (connList.contains(callobj)){
        index = connList.indexOf(callobj);
        connList.remove(index);
        name = userList.remove(index);
        playerList.remove(index);
        callobj.callback("Bye!");
        for (int i = 0; i < connList.size(); i++){
          if (index != i){
            connList.get(i).callback(name+" has left the chat");
          }
        }
        return "";
      }else{
        if (!silent)callobj.callback("You are not loged in!");
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
