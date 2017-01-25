import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Iperfer
{
    public static final String CMDLINE_ARG_ERROR = "Error: missing or additional arguments";
    public static final String PORT_RANGE_ERROR = "Error: port number must be in the range 1024 to 65535";
    public static final int MIN_PORT_NUMBER = 1024;
    public static final int MAX_PORT_NUMBER = 65535;
    
    public static final boolean DEBUG = true;
    
    /** Helper class for parsing integers without exception boilerplate */
    private static final class IntParseResult
    {
        public final boolean success;
        public final int val;
        
        public IntParseResult(boolean success, int val)
        {
            this.success = success;
            this.val = val;
        }
    }
    
    /** Wrapper method for exiting with message */
    private static void exit(String error)
    {
        System.out.println(error);
        System.exit(-1);
    }
    
    /** Wrapper method for parsing integers without exception boilerplate */
    private static IntParseResult ParseInt(String str)
    {
        try
        {
            int val = Integer.parseInt(str);
            return new IntParseResult(true, val);
        }
        catch (NumberFormatException e)
        {
            return new IntParseResult(false, 0);
        }
    }
    
    private static void RunClient(String hostname, int port, int time)
    {
        try
        {
            Socket socket = new Socket(hostname, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            
            long start_time = System.currentTimeMillis();
            long current_time = start_time;
            
            if (DEBUG)
                System.out.println("Client started at: " + start_time);
            
            long bytes_sent = 0;
            
            while (current_time - start_time < time * 1000)
            {
                char[] empty_buffer = new char[1000];
                writer.write(empty_buffer);
                bytes_sent += 1000;
                current_time = System.currentTimeMillis();
            }

            if (DEBUG)
                System.out.println("Client ended at: " + System.currentTimeMillis());
            
            writer.close();
            socket.close();
            
            // print metrics
            double rate = bytes_sent / (1e6 * time); // mbps
            System.out.println("sent=" + (bytes_sent / 1000) + " KB" +  "rate=" + rate + " Mbps");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void RunServer(int port)
    {
        if (DEBUG)
            System.out.println("Server starting...");
        
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            if (DEBUG)
                System.out.println("Server connection established!");
            
            long start_time = System.currentTimeMillis();
            long bytes_received = 0;
            while (!serverSocket.isClosed())
            {
                char[] buffer = new char[1000];
                bytes_received += br.read(buffer);
            }
            
            long end_time = System.currentTimeMillis();
            long time_range = end_time - start_time;
            
            if (DEBUG)
                System.out.println("Server ending....");
            
            br.close();
            clientSocket.close();
            serverSocket.close();
            
            // print metrics
            double rate = bytes_received / (1e6 * (time_range / 1e3)); // mbps
            System.out.println("received=" + (bytes_received / 1000) + " KB" +  "rate=" + rate + " Mbps");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        boolean is_client = false;
        boolean is_server = false;
        String server_hostname = null;
        int port = -1;
        int generation_time = -1;
        
        for (int i = 0; i < args.length; i++)
        {
            IntParseResult r;
            
            switch (args[i])
            {
                case "-h":
                    if (i + 1 >= args.length)
                        exit(CMDLINE_ARG_ERROR);
                    
                    server_hostname = args[i + 1];
                    i++;
                    break;
                    
                case "-p":
                    if (i + 1 >= args.length)
                        exit(CMDLINE_ARG_ERROR);
                    
                   r = ParseInt(args[i + 1]);
                   if (!r.success)
                       exit(CMDLINE_ARG_ERROR); // int parse failed

                   port = r.val;
                   i++;
                   break;
                   
                case "-t":
                    if (i + 1 >= args.length)
                        exit(CMDLINE_ARG_ERROR);
                    
                    r = ParseInt(args[i + 1]);
                    if (!r.success)
                        exit(CMDLINE_ARG_ERROR);
                    
                    generation_time = r.val;
                    i++;
                    break;
                    
                case "-c":
                    is_client = true;
                    break;
                    
                case "-s":
                    is_server = true;
                    break;
                    
                default:
                    exit(CMDLINE_ARG_ERROR);
            }
        }
        
        // arg checks
        if (is_client && is_server)
            exit(CMDLINE_ARG_ERROR);

        // port not set or out of range
        if (port < -1)
            exit(CMDLINE_ARG_ERROR);
        else if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER)
            exit(PORT_RANGE_ERROR);
        
        if (is_client)
        {
            if (server_hostname == null || generation_time == -1)
                exit(CMDLINE_ARG_ERROR);
        }
        else
        {
            if (server_hostname != null || generation_time != -1)
                exit(CMDLINE_ARG_ERROR);
        }
        
        // execute
        if (is_client)
            RunClient(server_hostname, port, generation_time);
        else
            RunServer(port);
    }
}
