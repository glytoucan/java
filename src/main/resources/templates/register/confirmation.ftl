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
  <form method="post" action="/Registries/complete" name="form1"> 
  ${Register[0]}
  <div>
    <table class="table table-bordered table-striped table-hover">
    <tr>
      <td>#</td>
      <td>Register?</td>
      <td>Original Structure</td>
      <td>Structure/GlycoCT</td>
      <td>Image</td>
    </tr>

<#list listNew as newItem>
    <tr>
      <td>
        ${newItem?counter}
      </td>
      <td>
        <div class="control-group">
          <div class="controls">
            <label class="checkbox">
              <input type="checkbox" checked="checked" name="listNew[${newItem?index}].register">
            </label>
          </div>
        </div>
      </td>
      <td>
        ${listNew[newItem?index].sequence?html?replace("\\n", "<br>")}
        <input type="hidden" name="listNew[${newItem?index}].sequence" value="${listNew[newItem?index].sequence}"/>
      </td>
      <td>
        ${listNew[newItem?index].resultSequence?html?replace("\\n", "<br>")}
        <input type="hidden" name="listNew[${newItem?index}].resultSequence" value="${listNew[newItem?index].resultSequence}"/>
      </td>
      <td>
        <img src="${newItem.image}" />
      </td>
    </tr>
</#list>
  </table>
  <div class="submit"><input  class="btn btn-primary" type="submit" value="Submit"/></div>
  </form>
  <hr />
  ${Error[0]}
  <div>
    <table class="table table-bordered table-striped table-hover">
    <tr>
      <td>#</td>
      <td>Original Structure</td>
      <td>Structure/GlycoCT</td>
      <td>Image</td>
    </tr>

<#list listErrors as newItem>
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
</#list>
  </table>
  </div>
  
  ${Registered[0]}
  <div>
    <table class="table table-bordered table-striped table-hover">
    <tr>
      <td>#</td>
      <td>Original Structure</td>
      <td>Structure/GlycoCT</td>
      <td>Image</td>
      <td>ID</td>
    </tr>

<#list listRegistered as newItem>
    <tr>
      <td>
        ${newItem?counter}
      </td>
      <td>
        ${listNew[newItem?index].sequence?html?replace("\\n", "<br>")}
      </td>
      <td>
        ${listNew[newItem?index].resultSequence?html?replace("\\n", "<br>")}
      </td>
      <td>
      <#if newItem.image?? >
        <img src="https://${hostname}/${newItem.image}" />
      <#else>
        no image
      </#if>
      </td>
      <td>
        ${newItem.id}
      </td>
    </tr>
</#list>
  </table>
  </div>
  Download this data.
<form action="/registries/download" id="downloadForm" method="post" accept-charset="utf-8">
<#list listNew as newItem>
  <input type="hidden" name="listNew[${newItem?index}].sequence" value="${listNew[newItem?index].sequence}"/>
  <input type="hidden" name="listNew[${newItem?index}].resultSequence" value="${listNew[newItem?index].resultSequence}"/>
</#list>
<#list listErrors as newItem>
  <input type="hidden" name="listErrors[${newItem?index}].sequence" value="${listErrors[newItem?index].sequence}"/>
  <input type="hidden" name="listErrors[${newItem?index}].resultSequence" value="${listNew[newItem?index].resultSequence}"/>
</#list>

  <div class="submit"><input  class="btn" type="submit" value="Download"/></div>
</form>
</div>
</div>
<#include "../footer.html">
</div>
</body>
</html>