<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">

<#include "../error.ftl">


<h1 class="page-header">${Title[0]}</h1>

<div class="container" style="padding:20px 0">

<table class="table table-striped">
<tbody>
<tr>
<td>${username}</td>
<td>${userProfile.givenName}</td>
</tr>
<tr>
<td>${email}</td>
<td>${userProfile.email}</td>
</tr>
<tr>
<td>Verified Email</td>
<td>${verifiedEmail?c}</td>
</tr>
<#--
<tr>
<td>${dateRegistered}</td>
<td>${userProfile.dateRegistered}</td>
</tr>
-->
</tbody>
</table>

<#-- ${Delete Account'), '/Users/delete', array('class' => 'btn' )); --> 
<#--
<?php $hit = count($glycanList); ?>
<?php if ($hit != 0) { ?>
<h2 class="page-header"><?php echo __('Contributed Structures:') ?> </h2>

<table class="table table-striped">
<thead>
<tr>
<th>#</th>
<th><?php echo __('accessionNumber') ?></th>
</tr>
</thead>
<tbody>
<?php foreach ($glycanList as $key => $value): ?>
<tr>
<td><?php echo ($key+1) ?></td>
<td><?php echo $value ?></td>
</tr>
<?php endforeach; ?>
</tbody>
</table>
<h3><?php echo __('Total Count') . ':' . $hit; ?></h3>
-->
				<li class="globalNavItem"><a class="globalNavItem_btn" href="/signout"><span class="globalNavItem_text">${signOut}</span></a></li>

</div>
</div>
<#include "../footer.html">

</div>
</body>
</html>