package com.hackerrank.sample.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hackerrank.sample.dto.FilteredProducts;
import com.hackerrank.sample.dto.SortedProducts;

@RestController
public class SampleController {

/*	final String uri = "https://jsonmock.hackerrank.com/api/inventory";
	
	   RestTemplate restTemplate = new RestTemplate();
	   String result = restTemplate.getForObject(uri, String.class);			
	   JSONObject root = new JSONObject(result);
	   
	   JSONArray data = root.getJSONArray("data");
	   */
	   
		
	final String uri = "https://jsonmock.hackerrank.com/api/inventory";
	JSONArray data;
	
	private RestTemplate restTemplate;
	public SampleController(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
		String result=restTemplate.getForObject(uri,String.class);
		JSONObject root=new JSONObject(result);
	    data = root.getJSONArray("data");
	}
	
		@CrossOrigin
		@GetMapping("/filter/price/{initial_price}/{final_price}") 
		private ResponseEntity< ArrayList<FilteredProducts> > filtered_books(@PathVariable("initial_price") int init_price , @PathVariable("final_price") int final_price)   
		{  
			
			try {
				if(init_price > final_price) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
						
					ArrayList<FilteredProducts> books = new ArrayList<FilteredProducts>();
					
					for(int i=0;i<data.length();i++) {
						JSONObject product = data.getJSONObject(i);
						int price=product.getInt("price");
						
						if(price>=init_price && price <=final_price) {
							books.add(new FilteredProducts(product.getString("barcode")));
						}
					}
					if(books.isEmpty()) {
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
			
				    return new ResponseEntity<ArrayList<FilteredProducts>>(books, HttpStatus.OK);  
			    
			}catch(Exception E)
				{
	   	System.out.println("Error encountered : "+E.getMessage());
	    return new ResponseEntity<ArrayList<FilteredProducts>>(HttpStatus.NOT_FOUND);
				}
			
		}  
		
		
		@CrossOrigin
		@GetMapping("/sort/price")  
		private ResponseEntity<SortedProducts[]> sorted_books()   
		{  
			
			try {
		         SortedProducts[] ans=new SortedProducts[data.length()];
		         
		         Integer[] index=new Integer[data.length()];

			for(int i=0;i<data.length();i++) {
				index[i]=i;
			}
			
			Arrays.sort(index,(a,b) -> Integer.compare(data.getJSONObject(a).getInt("price"),
					data.getJSONObject(b).getInt("price")));
			
			for(int i=0;i<index.length;i++) {
				ans[i]=new SortedProducts(data.getJSONObject(index[i]).getString("barcode"));
			}
		         
	
			    return new ResponseEntity<SortedProducts[]>(ans, HttpStatus.OK);
			    
			}catch(Exception E)
				{
	   	System.out.println("Error encountered : "+E.getMessage());
	    return new ResponseEntity<SortedProducts[]>(HttpStatus.NOT_FOUND);
				}
			
		}  	
	
}






