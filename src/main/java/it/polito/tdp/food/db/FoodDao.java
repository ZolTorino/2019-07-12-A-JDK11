package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Arco;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	public void listVertices(int x, Map<Integer,Food> idMap){
		String sql = "SELECT f.food_code as code, f.display_name as name "
				+ "FROM food as f, `portion` as p "
				+ "WHERE p.food_code=f.food_code "
				+ "GROUP BY f.display_name "
				+ "HAVING COUNT(distinct(p.portion_id))<=? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			st.setInt(1, x);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					idMap.put(res.getInt("food_code"),new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
	public void listEdges(int x, List<Arco> archi, Map<Integer,Food> idMap){
		String sql = "SELECT f1, f2, avg(c1.condiment_calories) as aver "
				+ "FROM ((SELECT f.food_code AS f1 "
				+ "		FROM food as f, `portion` as p "
				+ "		WHERE p.food_code=f.food_code "
				+ "		GROUP BY f.display_name "
				+ "		HAVING COUNT(distinct(p.portion_id))<=?) AS f1 , "
				+ "		(SELECT f.food_code as f2 "
				+ "		FROM food AS f, `portion` as p "
				+ "		WHERE p.food_code=f.food_code "
				+ "		GROUP BY f.display_name "
				+ "		HAVING COUNT(distinct(p.portion_id))<=?) AS f2, "
				+ "		food_condiment as fc1, food_condiment as fc2, condiment as c1, condiment as c2) "
				+ "WHERE f1= fc1.food_code AND fc1.condiment_code=c1.condiment_code "
				+ "AND f2= fc2.food_code AND fc2.condiment_code=c2.condiment_code "
				+ "AND f1> f2 "
				+"AND c1.condiment_code=c2.condiment_code "
				+ "GROUP BY f1, f2 "
				+ "HAVING COUNT(c2.condiment_code)>0 "
				+ "  order by avg(c1.condiment_calories) DESC" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			st.setInt(1, x);
			st.setInt(2, x);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					archi.add(new Arco(idMap.get(res.getInt("f1")),idMap.get(res.getInt("f2")),res.getDouble("aver")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
}
