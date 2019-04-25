<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title>Tarent Store</title>
<link rel="stylesheet" href="../resources/css/bootstrap.min.css"> 
<link rel="stylesheet" href="../resources/css/bootstrap-theme.min.css">
<style>
/*
 * Style tweaks
 * --------------------------------------------------
 */
html, body {
	overflow-x: hidden; /* Prevent scroll on narrow devices */
}

body {
	padding-top: 70px;
}

footer {
	padding: 30px 0;
}

/*
 * Off Canvas
 * --------------------------------------------------
 */
@media screen and (max-width: 767px) {
	.row-offcanvas {
		position: relative;
		-webkit-transition: all .25s ease-out;
		-o-transition: all .25s ease-out;
		transition: all .25s ease-out;
	}
	.row-offcanvas-right {
		right: 0;
	}
	.row-offcanvas-left {
		left: 0;
	}
	.row-offcanvas-right
  .sidebar-offcanvas {
		right: -50%; /* 6 columns */
	}
	.row-offcanvas-left
  .sidebar-offcanvas {
		left: -50%; /* 6 columns */
	}
	.row-offcanvas-right.active {
		right: 50%; /* 6 columns */
	}
	.row-offcanvas-left.active {
		left: 50%; /* 6 columns */
	}
	.sidebar-offcanvas {
		position: absolute;
		top: 0;
		width: 50%; /* 6 columns */
	}
}
</style>

</head>
<body>
	<nav class="navbar navbar-fixed-top navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="../">Tarent Store Reporting</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse" style="float:right">
				<ul class="nav navbar-nav">
					 <li class="active"><a href="../logout" ></a></li> 
				</ul>
			</div>
			<!-- /.nav-collapse -->
		</div>
		<!-- /.container -->
	</nav>
	<!-- /.navbar -->

	<div class="container">

		<div class="row row-offcanvas row-offcanvas-right">

			<div class="col-xs-12 col-sm-12">
				<p class="pull-right visible-xs">
					<button type="button" class="btn btn-primary btn-xs"
						data-toggle="offcanvas">Toggle nav</button>
				</p>
				<div class="container-fluid">
					<h3>Carts Reporting</h3>
					<p>List of all checked-out carts</p>
				</div>


				<div class="container-fluid">
					<h3></h3>

					<table class="table table-hover">
						<tr class="info">
							<td>Cart URI</td>
							<td>Cart Owner</td>
							<td>Items</td>
							<td>Total</td>
							<td>Checkout Date</td>
						
						</tr>
				 		<c:forEach var="cart" items="${list}">
							<tr>
							    <td>  <a href="/cart/${cart.user.id}/${cart.id}">/cart/${cart.user.id}/${cart.id}</a></td>
								<td>${cart.id}</td>
								<td>${cart.user.username}</td>
								<td>
								<ul>
								<c:forEach var="cartItem" items="${cart.cartItems}">
									<li>${cartItem.quantity}  ${cartItem.product.name}</li>
								</c:forEach> 
								</ul>
								</td>
								<td>${cart.total} EUR</td>
								<td>${cart.checkedOutAt}</td>
							
							</tr>
						</c:forEach> 

					</table>
				</div>

			</div>
			<!--/.col-xs-12.col-sm-9-->

			<div class="col-xs-6 col-sm-3 sidebar-offcanvas" id="sidebar">
				<div class="list-group">
					<!--  <a href="#" class="list-group-item active">Link</a> -->

				</div>
			</div>
			<!--/.sidebar-offcanvas-->
		</div>
		<!--/row-->

		<hr>

		<footer>
			<p></p>
		</footer>

	</div>
	<!--/.container-->



</body>
</html>
