import java.io.UnsupportedEncodingException;
import java.security.*;
import java.sql.*;
import java.util.*;


public class PopulateDatabase 
{
	public static String md5(String password) throws NoSuchAlgorithmException
	{
		
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < byteData.length; i++) 
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		ArrayList<String> userList = new ArrayList<String>(Arrays.asList("seshu","divya","praveen","teja","vamsi"));
		
		int max_age = 60;
		int min_age = 22;
		
		
		Random rand = new Random(); 

		
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = DriverManager.getConnection(  "jdbc:mysql://localhost:3306/contracts_database","seshu","opentext");
		
			
			for(String userTag : userList)
			{
				for(int i=1;i<=1000;i++)
				{
					String username = userTag+i;
					String password = md5(username);
					int age = rand.nextInt((max_age - min_age) + 1) + min_age;
					
					String query = "insert into users(username,password,age) values('"+username+"','"+password+"',"+age+")";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					preparedStmt.execute();
				}
				System.out.println(userTag + " users created");
			}
			
			
			for(int i=1000;i<30000000;i++)
			{
				String userTag = userList.get(rand.nextInt(userList.size()));
				int userExtend = rand.nextInt(1000) + 1;
				
				int contractValue = (rand.nextInt(1000)+1)*10000;
				String createdBy = userTag+userExtend;
				
				String query = "INSERT INTO contracts SET contract_value = "+contractValue+",created_by = (SELECT username FROM users WHERE username = '"+createdBy+"')";
				
				PreparedStatement preparedStmt = con.prepareStatement(query);
				preparedStmt.execute();
				
				if(i%1000==0)
					System.out.println(i+" posts created");
				
			}
			
			con.close();  
		}
		catch(Exception e){ System.out.println(e);}
	}  
}