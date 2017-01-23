	<nav class="globalNav">
		<div class="globalNav_inner clearfix">
			<a class="globalNav_logo" href="/">
				<img class="globalNav_toucan" src="/img/logo_toucan.png" height="38" width="67" alt="" />
				<span class="globalNav_logoText"><img src="/img/logo_text.png" height="40" width="90" alt="" /></span>
			</a> 
			<ul class="globalNav_ul">
				<li class="globalNavItem">
					<div class="globalNavItem_group">
						<a class="globalNavItem_btn globalNavItem_btn-pulldown" data-toggle="dropdown" href="#">
							<span class="globalNavItem_text">${registration}</span>
						</a>
						<ul class="dropdown-menu globalNavItem_sub">
						    <li><a href="/Registries/graphical">${byGraphic}</a></li>
						    <li><a href="/Registries/index">${byText}</a></li>
						    <li><a href="/Registries/upload">${fileUpload}</a></li>
					    </ul>
					</div>
			    </li>

<#--
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
-->
				<li class="globalNavItem">
					<div class="globalNavItem_group">
						<a class="globalNavItem_btn globalNavItem_btn-pulldown" data-toggle="dropdown" href="#">
							<span class="globalNavItem_text">${search}</span>
						</a>
						<ul class="dropdown-menu globalNavItem_sub">
						    <li><a href="/Structures/graphical">${byGraphic}</a></li>
						    <li><a href="/Structures/structureSearch">${byText}</a></li>
						    <li><a href="/Motifs/search">${byMotif}</a></li>
						    <li><a href="/Structures/web">${byWeb}</a></li>
						</ul>
					</div>
				</li>

				<li class="globalNavItem">
					<div class="globalNavItem_group">
						<a class="globalNavItem_btn globalNavItem_btn-pulldown" data-toggle="dropdown" href="#">
							<span class="globalNavItem_text">${viewAll}</span>
						</a>
						<ul class="dropdown-menu globalNavItem_sub">
						    <li><a href="/Motifs/listAll">${motifList}</a></li>
						    <li><a href="/Structures">${glycanList}</a></li>
						</ul>
					</div>
				</li>

				<li class="globalNavItem"><a class="globalNavItem_btn" href="/Preferences/index"><span class="globalNavItem_text">${preferences}</span></a></li>
<#if user??>
				<li class="globalNavItem"><a class="globalNavItem_btn" href="/Users/profile"><span class="globalNavItem_text">${profile}</span></a></li>
				<li class="globalNavItem"><a class="globalNavItem_btn" href="/signout"><span class="globalNavItem_text">${signOut}</span></a></li>
<#else>
				<li class="globalNavItem"><a class="globalNavItem_btn" href="/login"><span class="globalNavItem_text"><img src="/img/signin.png" /></span></a></li>
</#if>
		</ul>
<!-- Accession Number search form -->
			<div class="globalNavSearch">
				<form method="post" action="/Structures/Accession">
					<input type="text" placeholder="${accessionNumber}" name="aNum" />
					<button class="globalNavSearch_btn" type="submit"></button>
				</form>
			</div>
<!-- /.Accession Number search form -->
		</div><!--/.globalNav_inner-->
	</nav>
