<!DOCTYPE html>
<html lang="ja">
<head>
	<title>Glycan Repository</title>
<#include "../header.html">
</head>
<body>
<a name="top"></a><!--link for page top-->
<div id="contents">
<#include "../nav.ftl">
<div class="container">
<#include "../error.ftl">
  <h1 class="page-header">${Title[0]}</h1>

<#list listNew>
  <form method="post" action="/Registries/complete"> 
  ${Register[0]}
  <div>
    <table class="table table-bordered table-striped table-hover">
    <tr>
      <td>#</td>
      <td>Register?</td>
      <td>Original Structure</td>
      <td>Structure</td>
      <td>Image</td>
    </tr>


<#items as newItem>
    <tr>
      <td>
        ${newItem?counter}
      </td>
      <td>
        <div class="control-group">
          <div class="controls">
            <label class="checkbox">
              <input type="checkbox" name="checked" checked="${listNew[newItem?index].register?c}">
            </label>
          </div>
        </div>
      </td>
      <td>
        ${listNew[newItem?index].sequence?html?replace("\\n", "<br>")}
        <input type="hidden" name="sequence" value="${listNew[newItem?index].sequence}"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      </td>
      <td>
        ${listNew[newItem?index].resultSequence?html?replace("\\n", "<br>")}
        <input type="hidden" name="resultSequence" value="${listNew[newItem?index].resultSequence}"/>
      </td>
      <td>
        <img src="${newItem.image}" />
        <input type="hidden" name="image" value="${newItem.image}"/>
      </td>
    </tr>
</#items>
  </table>
  <div class="submit"><input  class="btn btn-primary" type="submit" value="Submit"/></div>
  </form>
  <hr />
<#else>
</#list>

<#list listErrors>

  ${Error[0]}
  <div>
    <table class="table table-bordered table-striped table-hover">
    <tr>
      <td>#</td>
      <td>Original Glycan Sequence</td>
      <td>Sequence</td>
      <td>Image</td>
    </tr>

<#items as newItem>
    <tr>
      <td>
        ${newItem?counter}
      </td>
      <td>
        ${newItem.sequence?html?replace("\n", "<br>")}
      </td>
      <td>
        ${newItem.resultSequence?html?replace("\\n", "<br>")}
      </td>
      <td>
      <#if newItem.image?? >
        <img src="${newItem.image}" />
      <#else>
        no image
      </#if>
      </td>
    </tr>
</#items>
  </table>
  </div>
<#else>
</#list>

<#list listRegistered>
  ${Registered[0]}
  <div>
    <table class="table table-bordered table-striped table-hover">
    <tr>
      <td>#</td>
      <td>Original Glycan Sequence</td>
      <td>Sequence</td>
      <td>Image</td>
      <td>ID</td>
    </tr>

<#items as newItem>
    <tr>
      <td>
        ${newItem?counter}
      </td>
      <td>
        ${listRegistered[newItem?index].sequence?html?replace("\\n", "<br>")}
      </td>
      <td>
        ${listRegistered[newItem?index].resultSequence?html?replace("\\n", "<br>")}
      </td>
      <td>
      <#if newItem.image?? >
        <img src="${newItem.image}" />
      <#else>
        no image
      </#if>
      </td>
      <td>
        ${newItem.id}
      </td>
    </tr>
</#items>
  </table>
  </div>
<#else>
</#list>
  
<#if ((listNew?size > 0) || (listErrors?size > 0) || (listRegistered?size > 0)) >

  Download this data.
<form action="/Registries/download" id="downloadForm" method="post" accept-charset="utf-8">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

<#list listNew>
<#items as newItem>
  <input type="hidden" name="sequence" value="${listNew[newItem?index].sequence}"/>
  <input type="hidden" name="resultSequence" value="${listNew[newItem?index].resultSequence}"/>
</#items>
<#else>
</#list>

<#list listErrors>
<#items as newItem>
  <input type="hidden" name="errorSequence" value="${listErrors[newItem?index].sequence}"/>
  <input type="hidden" name="errorResultSequence" value="${listErrors[newItem?index].resultSequence}"/>
</#items>
<#else>
</#list>

<#list listRegistered>
<#items as newItem>
  <input type="hidden" name="registeredId" value="${listRegistered[newItem?index].id}"/>
  <input type="hidden" name="registeredSequence" value="${listRegistered[newItem?index].sequence}"/>
  <input type="hidden" name="registeredResultSequence" value="${listRegistered[newItem?index].resultSequence}"/>
</#items>
<#else>
</#list>

  <div class="submit"><input  class="btn" type="submit" value="Download"/></div>
</form>

</#if>

</div>
</div>
<#include "../footer.html">
</div>
</body>
</html>