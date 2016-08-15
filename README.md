# WebviewPokeIv

這是個用 Webview 開 [https://pokeiv.net/](https://pokeiv.net/) 的 App，  
主要目的是讓使用者在手機上查 iv 更方便。  

原本使用者在 [https://pokeiv.net/](https://pokeiv.net/) 使用 Google 帳號的登入步驟是  

1.  於 pokeiv登入頁點擊 Google 登入
2.  在 Google 登入的流程登入成功＆確認授權
3.  在 授權成功頁，手動複製授權碼
4.  開啟 [https://pokeiv.net/](https://pokeiv.net/) 登入頁貼上
5.  點擊登入
  
  
但由於  

1.  Google 授權成功頁，在手機上的介面非常難點，很難正確複製授權碼
2.  複製完授權碼後，pokeiv 的登入頁已經被蓋掉了，使用者必須想辦法開新分頁，或重新搜尋出 pokeiv 登入頁

因此，這個 App 會在偵測到 webview 開啟 google 授權成功頁後，  

1.  自動取出頁面上顯示的 Google 授權碼
2.  幫使用者貼回 [https://pokeiv.net/](https://pokeiv.net/) 登入頁的授權碼匡中  

幫使用者省掉複製貼上的步驟，因此流程變成  

1.  於 pokeiv登入頁點擊 Google 登入
2.  在 Google 登入的流程登入成功＆確認授權
（接著等待畫面自動跳轉回 pokeiv 登入頁並自動填好 google 授權碼）
3.  點擊登入
  
[注意]這是非 [https://pokeiv.net/](https://pokeiv.net/) 官方 App，純粹為了手機瀏覽方便而開發。  
這種東西不可能去 google play 上架，所以才傳在 github，  
希望能稍微節省其他人的時間
