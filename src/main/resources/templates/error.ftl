<#if errorMessage??>
<div class="message">
  <div class="errorMessage">
    <p><img src="/img/error.png" height="16" width="16" alt="" />&nbsp;&nbsp;&nbsp;${errorMessage}</p>
  </div>
</div>
</#if>
<#if warningMessage??>
<div class="message">
  <div class="warningMessage">
    <p><img src="/img/warning.png" height="16" width="16" alt="" >&nbsp;&nbsp;&nbsp;${warningMessage}</p>
  </div>
</div>
</#if>
<#if infoMessage??>
<div class="message">
  <div class="informationMessage">
    <p><img src="/img/information.png" height="16" width="16" alt="" >&nbsp;&nbsp;&nbsp;${infoMessage}</p>
  </div>
</div>
</#if>

<!-- error -->
<#if status??>
status:${status}
</#if>
<#if error??>
<div class="message">
  <div class="errorMessage">
    <p><img src="/img/error.png" height="16" width="16" alt="" />&nbsp;&nbsp;&nbsp;${error}</p>
  </div>
</div>
</#if>
<#if message??>
<div class="message">
  <div class="errorMessage">
    <p><img src="/img/error.png" height="16" width="16" alt="" />&nbsp;&nbsp;&nbsp;${message}</p>
  </div>
</div>
</#if>
<#-- [timestamp, status, error, message, path, hostname] -->
