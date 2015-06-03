  	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
  	<script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>
  	<script type="text/javascript">bkLib.onDomLoaded(nicEditors.allTextAreas);</script>
						
<?php
	session_start();
	include('../topbar.vm');
	$e = 0;
	if(isset($_GET['error'])){
		$e = 1;
	}
?>

<div id="newGuidePage">
	<?php
	if(!isset($_SESSION['id']) AND !isset($_SESSION['pseudo'])){
		?>
		<div id="coError"> vous devez être connécté pour afficher cette page !</div>
		<?php
	}else{
	?>
	<div id="pub1"></div>
	<div id="newGuide">

		<span style="font-size:30px;">Proposer un nouveau guide :</span><br><br>
		<?php
		if($e == 1){
		?>
		<div id="regError" style="width:610;">
			ATTENTION : Tout les champs sont requis !
		</div>
		<?php
		}
		?>
		<div id="indications">
			Proposer un guide est la meilleure façon de participer à la vie de PlanMyTrip.<br>
			Ce guide sera accessible à tout les visiteurs de PlanMyTripp (inscrits ou non!).<br>
			Chaque guide doit être validé par un membre de notre équipe de modération (sous 48h).<br>
		</div>
		<span style="font-weight:bold">Evitez les fautes d'orthographes, soyez le plus clair et précis possible.</span><br>
		<form method=post action="http://<?php echo $_SERVER['HTTP_HOST'] ?>/PlanMyTrip/pages/new_guide/post.php">
			Pour commencer, entrez les informations concernant votre séjour :<br><br>
			<table>
			<tr>
				<td>Entrez un titre à votre guide </td>
				<td><input style="height:30px;width:200px;" type=text name="titre" placeholder=" Titre"></td>
			</tr>
			<tr>
				<td>Pays</td>
				<td><input style="height:30px;width:200px;" type=text name="pays" placeholder=" Pays"></td>
			</tr>
			<tr>
				<td>Ville</td>
				<td><input style="height:30px;width:200px;" type=text name="ville" placeholder=" Ville"></td>
			</tr>
			<tr>
				<td style="width:150px;">durée du séjour</td>
				<td>
					<SELECT style="width:200px;" name="duration" id="duration">
					<OPTION VALUE="1"> </OPTION>
					<OPTION VALUE="1">1</OPTION>
					<OPTION VALUE="2">2</OPTION>
					<OPTION VALUE="3">3</OPTION>
					<OPTION VALUE="4">4</OPTION>
					<OPTION VALUE="5">5</OPTION>
					<OPTION VALUE="6">6</OPTION>
					<OPTION VALUE="7">7</OPTION>
					<OPTION VALUE="8">8</OPTION>
					<OPTION VALUE="9">9</OPTION>
					<OPTION VALUE="10+">10+</OPTION>
					</SELECT>
				</td>
			</tr>
			</table>
			<br>

			À présent, partagez votre experience...<br>
			Conseil : proposez votre guide sous forme de liste.
			<div id="aNewGuide">
			</div>
			<br>
			<textarea id="input" cols="80" rows="20" name="input"></textarea>

			N'hésitez pas à ajouter des photos, des cartes ou des liens en tout genre.<br>
			Les itinéraires Google Maps ou autres sont les bienvenues.<br><br>
		
			<input type="submit" style="display:block;margin:auto;width:300px;" value="Soumettre" id="propose" name="loginEnt"><br><br>
		</form>

		
	</div>
	<div id="help">
		Voir un exemple de guide rédigé
	</div>
	<?php } ?>
</div>
	<script src="http://maps.googleapis.com/maps/api/js?sensor=false&amp;libraries=places"></script>
  	<script type="text/javascript" src="http://<?php echo $_SERVER['HTTP_HOST'] ?>/PlanMyTrip/js/jquery.geocomplete.min.js"></script>
    <script>
        function makeAdress(div, name, nb){
        	var inputAdress = $('<input id='+name+' />');
        	var inputNote = $('<textarea></textarea>');
        	var divAdresse = $('<div id='+name+'Adress />');
        	divAdresse.css('margin-left','20px');
        	divAdresse.css('margin-bottom','10px');
        	inputAdress.css('margin-bottom','10px');
        	inputAdress.css('margin-left','10px');
        	inputNote.css('margin-left','10px');
        	divAdresse.append('Adresse '+nb+' :<br>');
        	inputAdress.geocomplete();
        	divAdresse.append(inputAdress);
        	divAdresse.append('<br>');
        	divAdresse.append(inputNote);
        	divAdresse.append('<br>');
        	div.append(divAdresse);
        }
        /*$('#duration').change(function(){
        	for (var i = 0 ; i < $('#duration option:selected').val(); i++) {
      			$('#aNewGuide').append('Jour '+i+' :');
        		for (var j = 0 ; j < 4 ; j++) {
        			makeAdress($('#aNewGuide'), 'adress'+j, j);
        		}
        	};
        });*/
    </script>
<?php include("../footer.php"); ?>