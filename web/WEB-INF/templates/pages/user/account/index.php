<?php
	session_start();
	include("../../../pages/topbar.vm");
	if (isset($_SESSION['id']) AND isset($_SESSION['pseudo'])){ 
	?>
		<div id="blanc" style="padding-top:70px;"></div>
		<div id ="accountManagment">
		
		</div>
		
		<script type="text/javascript" src="script.js"></script>
		<?php 
	} 
	else { 
		?>
		<div id="blanc" style="padding-top:200px;"></div>
		<div id="coError" style="width:500px;margin:auto;"> vous devez être connécté pour afficher cette page !</div>
		<?php 
	} 
	?>
	<div id="user_content">
		<div id="sub_content"></div>
		</div>
	<?php include("../../footer.php"); 
?>