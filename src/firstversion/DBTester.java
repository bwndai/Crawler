package firstversion;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;

public class DBTester {
	public static DB db = new DB();
	public static void main(String[] args) throws SQLException, IOException{
		//truncate a table: empty it completely;
		db.runSql2("TRUNCATE RECORD;");
		processPage("http://www.mit.edu");
	}
	public static void processPage(String URL) throws SQLException, IOException{
		// TODO Auto-generated method stub
		String sql = "select * from Record where URL = '" + URL + "'";
		ResultSet rs = db.runSql(sql);
		//check if the given URL is already in database
		if (rs.next()){
			
		}else{
			//if not, store the URL to database to avoid parsing again
			// ? is a undefined value,
			sql = "INSERT INTO `Crawler`.`Record`" + "(`URL`) VALUES" + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.execute();
			Document doc = Jsoup.connect(URL).get();
			if(doc.text().contains("research")){
				System.out.println(URL);
			}
			
			Elements questions = doc.select("a[href]");
			for (Element link: questions){
				if (link.attr("abs:href").contains("http") && !link.attr("abs:href").contains(".pdf") )
					processPage(link.attr("abs:href"));
			}
		}
		
	}

}
