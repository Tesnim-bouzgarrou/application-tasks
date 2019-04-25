package de.tarent.challenge.store.reporting;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tarent.challenge.store.carts.Cart;
import de.tarent.challenge.store.carts.CartService;
import de.tarent.challenge.store.carts.CartStatus;

@Controller
public class ReportingController {
	
	@Autowired
	private  CartService cartService;

	@RequestMapping("/reporting")
	public String checkedOutCarts(ModelMap model) {
		
		   List<Cart> carts = cartService.getCartsByStatus(CartStatus.ORDERED);
		   model.addAttribute("list", carts);  
	          
		return  "reporting";
	}

}