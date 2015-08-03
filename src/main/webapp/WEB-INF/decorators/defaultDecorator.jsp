<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=940" />
    <title><sitemesh:write property='title'/></title>
<%-- <title><?php echo __('Glycan') ?> <?php echo __('Repository') ?></title> --%>

	<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css" /> 
	<link rel="stylesheet" type="text/css" href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" />
	<link rel="stylesheet" type="text/css" href="/css/GTC.css" />
	<link rel="stylesheet" type="text/css" href="/css/glycan_list_new.css" />
	<link href="/favicon.ico" type="image/x-icon" rel="icon" />
	<link href="/favicon.ico" type="image/x-icon" rel="shortcut icon" />
    <sitemesh:write property='head'/>
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
	<nav class="globalNav">
		<div class="globalNav_inner clearfix">
			<a class="globalNav_logo" href="/">
				<img class="globalNav_toucan" src="/img/logo_toucan.png" height="38" width="67" alt="" />
				<span class="globalNav_logoText"><img src="/img/logo_text.png" height="40" width="90" alt="" /></span>
			</a> 
			<ul class="globalNav_ul">
			<%-- 
			<?php if($user) { ?>
				<li class="globalNavItem">
					<div class="globalNavItem_group">
						<a class="globalNavItem_btn globalNavItem_btn-pulldown" data-toggle="dropdown" href="#">
							<span class="globalNavItem_text"><?php echo __($common_doc['registration']) ?></span>
						</a>
						<ul class="dropdown-menu globalNavItem_sub">
						    <li><?php echo $this->Html->link($common_doc['byGraphic'], '/Registries/graphical') ?></li>
						    <li><?php echo $this->Html->link($common_doc['byText'], '/Registries/index') ?></li>
						    <li><?php echo $this->Html->link($common_doc['fileUpload'], '/Registries/upload') ?></li>
					    </ul>
					</div>
			    </li>
			<?php } ?>

			<?php if($mod) { ?>
				<li class="globalNavItem">
					<div class="globalNavItem_group">
						<a class="globalNavItem_btn globalNavItem_btn-pulldown" data-toggle="dropdown" href="#">
							<span class="globalNavItem_text"><?php echo __('Admin') ?></span>
						</a>
						<ul class="dropdown-menu globalNavItem_sub">
						    <li><?php echo $this->Html->link('User List', '/Users/listAll') ?></li>
						    <li><?php echo $this->Html->link('Motif Management', '/Registries/motif') ?></li>
						    <li><?php echo $this->Html->link('Glycans List', '/Structures/glycansList') ?></li>
					    </ul>
					</div>
			    </li>
			<?php } ?>
			--%>

				<li class="globalNavItem">
					<div class="globalNavItem_group">
						<a class="globalNavItem_btn globalNavItem_btn-pulldown" data-toggle="dropdown" href="#">
								<c:set var="search" value="${search}" scope="request" />
							<span class="globalNavItem_text">${search}</span>
						</a>
						<ul class="dropdown-menu globalNavItem_sub">
						    <li><a href="${pageContext.request.contextPath}/Structures/graphical">{{byGraphic}}</a></li>
						    <li><a href="/Structures/structureSearch">{{byText}}</a></li>
						    <li><a href="/Structures/search">{{byMotif}}</a></li>
						</ul>
					</div>
				</li>

				<li class="globalNavItem">
					<div class="globalNavItem_group">
						<a class="globalNavItem_btn globalNavItem_btn-pulldown" data-toggle="dropdown" href="#">
							<span class="globalNavItem_text">{{viewAll}}</span>
						</a>
						<ul class="dropdown-menu globalNavItem_sub">
						    <li><a href="/Motifs/listAll">{{motifList}}</a></li>
						    <li><a href="/Structures">{{glycanList}}</a></li>
						</ul>
					</div>
				</li>

				<li class="globalNavItem"><a class="globalNavItem_btn" href="/Preferences/index"><span class="globalNavItem_text">{{preferences}}</span></a></li>
				<%-- 
			<?php if($user): ?>
				<li class="globalNavItem"><a class="globalNavItem_btn" href="/Users/profile"><span class="globalNavItem_text"><?php echo($common_doc['profile']); ?></span></a></li>
			<?php endif; ?>
			<?php if(!$user): ?>
				<li class="globalNavItem"><a class="globalNavItem_btn" data-toggle="modal" href="#SignIn"><span class="globalNavItem_text"><?php echo($common_doc['signIn']); ?></span></a></li>
				<li class="globalNavItem"><a class="globalNavItem_btn" data-toggle="modal" href="#SignUp"><span class="globalNavItem_text"><?php echo($common_doc['signUp']); ?></span></a></li>
			<?php endif; ?>
			<?php if($user): ?>
				<li class="globalNavItem"><a class="globalNavItem_btn" href="/Users/out"><span class="globalNavItem_text"><?php echo($common_doc['signOut']); ?></span></a></li>
			<?php endif; ?>
			  --%>
			</ul>
<!-- Accession Number search form -->
			<div class="globalNavSearch">
				<form method="post" action="/Structures/Accession">
					<input type="text" placeholder="<?php echo $common_doc['accessionNumber'] ?>" name="aNum" />
					<button class="globalNavSearch_btn" type="submit"></button>
				</form>
			</div>
<!-- /.Accession Number search form -->
		</div><!--/.globalNav_inner-->
	</nav>

<!--Start Error Message-->
<%--
<?php echo $this->Session->flash() ?>
--%>
<!--END Error Message-->

<sitemesh:write property='body'/>
<%--
<?php if ($this->name == 'Stanzas' && $this->action == 'index'): ?>
	<?php echo $this->fetch('content'); ?>
<?php else: ?>
	<div class="container"><?php echo $this->fetch('content'); ?></div>
<?php endif; ?>


<!-- SignUp Modal form -->
	<div class="modal hide fade" id="SignUp">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">x</button>
			<h3>{{Sign Up}}></h3>
		</div>
		<?php echo $this->Form->create( false, array('type'=>'post' , 'url'=>'/Users/up', 'id' => 'indexForm_signup' )); ?>
	    <div class="modal-body">
	        <br>
	        <?php echo __('The username can contain uppercase/lowercase letters, numbers, and the special chars (-, _) only.  The length should be between 3 to 15 characters.') ?>
	        <?php echo $this->Form->input(__('Username'),array('value' => "", 'id' => 'SignupUsername')); ?>
	        <br>
	        <?php echo __('The password length must be longer than four characters, have both uppercase and lowercase characters, one numeric, and one or more of the following special characters (!@#$%^&amp;*).') ?>
	        <?php echo $this->Form->input(__('Password'),array('type' => 'password' , 'value' => "", 'id' => 'SignupPassword', 'label' => array( 'text' => 'Password') )); ?>
	        <?php echo $this->Form->input(__('Confirm Password'),array('type' => 'password' , 'value' => "", 'id' => 'Confirm_Password', 'label' => array( 'text' => 'Confirm Password') )); ?>
	        <?php echo $this->Form->input(__('Full Name'),array('value' => "", 'id' => 'Full_Name', 'label' => array( 'text' => 'Full Name') )); ?>
	        <?php echo $this->Form->input(__('Email'),array('value' => "", 'id' => 'SignupEmail', 'label' => array( 'text' => 'Email') )); ?>
	        <?php echo $this->Form->input(__('Affiliation'),array('value' => "", 'id' => 'Affiliation', 'label' => array( 'text' => 'Affiliation') )); ?>
	    </div>
	    <div class="modal-footer">
	        <?php echo $this->Form->submit(__('submit'), array('class' => 'btn')); ?><br>
	        <?php echo __('Please note that even after registration, you will not be able to access until a moderator has authorized the login.') ?>
	    </div>
		<?php echo $this->Form->end(); ?>
	</div><!--/.modal hide fade #SignUp-->

<!-- Sign in Modal form -->
	<div class="modal hide fade" id="SignIn">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">x</button>
			<h3><?php echo __($menuDoc->signIn_titleLabel) ?></h3>
		</div>
		<?php echo $this->Form->create( false, array('type'=>'post' , 'url'=>'/Users/in', 'id' => 'indexForm_signin' )); ?>
	    <div class="modal-body">
	        <?php echo $this->Form->input($common_doc['username'],array('value' => "", 'id' => 'SigninUsername', 'label' => array( 'text' => $common_doc['username']) )); ?>
	        <?php echo $this->Form->input($common_doc['password'],array('type' => 'password' , 'value' => "", 'id' => 'SigninPassword', 'label' => array( 'text' => $common_doc['password']) )); ?>
	    </div>
	    <div class="modal-footer">
	        <?php echo $this->Html->link($common_doc['emailPassword'], '/Users/emailpwform', array('class' => 'btn')); ?>
	        <?php echo $this->Html->link($common_doc['recoverUsername'], '/Users/recoveruserform', array('class' => 'btn')); ?>
	        <?php echo $this->Form->button($common_doc['signIn'], array('class' => 'btn')); ?>
	    </div>
		<?php echo $this->Form->end(); ?>
	</div><!--/.modal hide fade #SignIn-->

	<?php echo $this->Html->script('https://code.jquery.com/jquery-1.10.2.min.js'); ?>
	<?php echo $this->Html->script('//code.jquery.com/ui/1.11.1/jquery-ui.js'); ?>

	<?php echo $this->Html->script('bootstrap.min'); ?>
	<?php echo $this->Html->script('confirmation'); ?>
	<?php echo $this->Html->script('glycan_list_new'); ?>
	<?php echo $this->Html->script('init'); ?>
	<script>
		$('.dropdownj-toggle').click(function() {
			$('.dropdown-toggle').dropdown('toggle');
		});
	</script>
--%>

	<footer class="footer">
		<!--<div class="footer_col footer_col-center">-->
		<div class="footer_wrapper">
			<div class="footer_inner_left">
					<span class="footer_share_text">Share</span>
					<ul class="footerShare clearfix">
						<li>
							<a escape="false" target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=http://glytoucan.org/"><img src="/img/share_fb.png" alt="" height="52" width="26"></a>
						</li>
						<li>
							<a escape="false" target="_blank" href="https://twitter.com/home?status=http://glytoucan.org/"><img src="/img/share_twitter.png" alt="" height="52" width="26"></a>
						</li>
						<li>
							<a escape="false" target="_blank" href="https://plus.google.com/share?url=http://glytoucan.org/"><img src="/img/share_google.png" alt="" height="52" width="26"></a>
						</li>
					</ul>
			</div><!--/.footer_inner-->
			<div class="footer_inner_top">
				<ul class="footerLinks clearfix">
					<li>
						<a class="termsAndConditions" href="http://code.glytoucan.org/termsAndConditions/" target="_blank" >Terms and Conditions</a>
					</li>
					<li>
						<a class="sitePolicy" href="http://code.glytoucan.org/sitePolicy/" target="_blank" >Site Policy</a>
					</li>
					<li>
						<a class="FAQ" href="http://code.glytoucan.org/FAQ/" target="_blank">FAQ</a>
					</li>
					<li>
						<a class="userGuide_stanzas" href="http://code.glytoucan.org/manual/" target="_blank">User Guide</a>
					</li>
					<li>
						<a class="footerMail" href="mailto:support@glytoucan.org">Contact Us</a>
					</li>
				</ul>
			</div><!--/.footer_inner_top-->
				<a class="footer_logo clearfix" href="#top">
						<img class="globalNav_toucan"  src="/img/logo_toucan.png" height="38" width="67" alt="" />
						<span class="footer_logoText"><img src="/img/logo_text_footer.png" height="40" width="80" alt="" /></span>
				</a> 
			<div class="footer_inner_bottom clearfix">
			<!--<div class="footer-bottom_">-->
				<ul class="footerLicense clearfix">
					<li>
						<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/80x15.png" /></a>
					</li>
					<li>
						<p>&nbsp;&nbsp;&nbsp;Copyright &copy; 2015 &nbsp;&nbsp; Glytoucan.org v1.0</p>
					</li>
				</ul>
			</div><!--/.footer_inner_bottom-->	
		</div><!--/.footer_wrapper-->	
	</footer><!--/.footer-->

</div><!--/#contents-->
     
<!-- google analytics tracking javascript -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-54566807-1', 'auto');
  ga('send', 'pageview');
</script>

</body>
</html>