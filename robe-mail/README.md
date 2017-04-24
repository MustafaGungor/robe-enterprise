# robe-mail
---
This module handles mail template creation, mail queue operations and mail authentication etc. These operations are handled in a developer friendly approach.
## Motivation
Creating a mail bundle which can provide following points. 
* easy to understand 
* configurable 
* templating solution included (Freemarker)
* queue support included.

## Getting Started
You have to complete 4 steps in order to start mail bundle
* Add dependency (Maven sample)

```xml
<dependency>
  <groupId>com.mebitech.robe.mail</groupId>
  <artifactId>robe-mail</artifactId>
</dependency>
```
* Decide the properties. It is directly matching with java Mail API.

```yml
mail:
    mailName1:
      usernameKey: 'mail.smtp.username'
      passwordKey: 'mail.smtp.password'
      properties:
        mail.smtp.username: test-name
        mail.smtp.password: test-password
        mail.smtp.host: smtp.live.com
        mail.smtp.port: 25
        mail.smtp.auth: true
        mail.smtp.starttls.enable: true
    mailName2:
      usernameKey: 'mail.smtp.username'
      passwordKey: 'mail.smtp.password'
      properties:
        mail.smtp.username: test-name
        mail.smtp.password: test-password
        mail.smtp.host: smtp.live.com
        mail.smtp.port: 25
        mail.smtp.auth: true
        mail.smtp.starttls.enable: true
```
   
* Inject `MailSender` in application

```java

@Qualifier("mailName")
@Autowired
RobeMailSender robeMailSender;

```



* Use `MailSender` class whenever you want to send mail

```java
MailItem mailItem = new MailItem();
mailItem.setBody("Some mail content");
mailItem.setReceivers(entity.getUsername());
mailItem.setTitle("Robe.io Password Change Request");
robeMailSender.sendMail(mailItem);
```

#### Mail Templating

##### Freemarker Sample

* Add dependency (Maven sample)

```
<!-- https://mvnrepository.com/artifact/org.freemarker/freemarker -->
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
</dependency>

```
* use sample

```java
Template template = null;
Map<String, Object> parameter = new HashMap<String, Object>();
Writer out = new StringWriter();
String body = mailTemplateOptional.get().getTemplate();
template = new Template("robeTemplate", body, cfg);
parameter.put("name", entity.getName());
parameter.put("surname", entity.getSurname());
template.process(parameter, out);
mailItem.setBody(out.toString());
```
Now it is ready for send mail.

##### Thymeleaf Sample

* Add dependency (Maven sample)

```
<!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf-spring3 -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring3</artifactId>
</dependency>

```
* Our email-inlineimage.html is the template file we will use for sending emails with an inlined image, and it looks like:

```html
<!--  ..resources/html/email-inlineimage.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <title th:remove="all">Template for HTML email with inline image</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  </head>
  <body>
    <p th:text="#{greeting(${name})}">
      Hello, Peter Static!
    </p>
    <p th:if="${name.length() > 10}">
      Wow! You've got a long name (more than 10 chars)!
    </p>
    <p>
      You have been successfully subscribed to the <b>Fake newsletter</b> on
      <span th:text="${#dates.format(subscriptionDate)}">28-12-2012</span>
    </p>
    <p>Your hobbies are:</p>
    <ul th:remove="all-but-first">
      <li th:each="hobby : ${hobbies}" th:text="${hobby}">Reading</li>
      <li>Writing</li>
      <li>Bowling</li>
    </ul>
    <p>
      You can find <b>your inlined image</b> just below this text.
    </p>
    <p>
      <img src="sample.png" th:src="|cid:${imageResourceName}|" />
    </p>
    <p>
      Regards, <br />
      <em>The Thymeleaf Team</em>
    </p>
  </body>
</html>
```
* Given the fact that our email processing is not web-dependent, an instance of Context will do:

```java
final Context ctx = new Context(locale);
ctx.setVariable("name", recipientName);
ctx.setVariable("subscriptionDate", new Date());
ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML
final String htmlContent = this.templateEngine.process("html/email-inlineimage.html", ctx);
mailItem.setBody(htmlContent);
```
Now it is ready for send mail.

## Details

### Mail Sending Operation
Here what happens at Queue and consumer thread implementation when you call `MailManager.sendMail(mailItem);` 
* Sender thread wakes up on every item insert and works until queue goes empty.
* Every item in queue will be consumed only once. 
* If any error occurs it is the developers responsibility to handle it. {@link com.mebitech.robe.mail.MailEvent}.