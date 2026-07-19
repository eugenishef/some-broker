package dev.eshevchenko.doc;

public final class ClientExamples {

  private ClientExamples() {}

  public static final String CREATE_CLIENT_REQUEST = """
      {
        "firstName": "Евгений",
        "lastName": "Петров",
        "middleName": "Игоревич",
        "birthDate": "1990-05-20",
        "email": "petrov@example.com",
        "phone": "+79991234567",
        "inn": "770123456789",
        "passport": {
          "series": "4510",
          "number": "123456"
        }
      }
      """;

  public static final String CREATE_CLIENT_RESPONSE = """
      {
        "clientId": "1",
        "status": "CREATED"
      }
      """;

  public static final String CLIENT_RESPONSE = """
      {
        "clientId": "1",
        "firstName": "Евгений",
        "lastName": "Петров",
        "middleName": "Игоревич",
        "birthDate": "1990-05-20",
        "email": "petrov@example.com",
        "phone": "+79991234567",
        "status": "ACTIVE",
        "accounts": []
      }
      """;

  public static final String UPDATE_CLIENT_REQUEST = """
      {
        "firstName": "Евгений",
        "lastName": "Петров",
        "middleName": "Игоревич",
        "birthDate": "1990-05-20",
        "email": "petrov@example.com",
        "phone": "+79991234567"
      }
      """;

  public static final String PATCH_CLIENT_REQUEST = """
      {
        "phone": "+79999999999"
      }
      """;

  public static final String SEARCH_CLIENT_REQUEST = """
      {
        "page": 0,
        "size": 20,
        "sort": [
          {
            "field": "createdAt",
            "direction": "DESC"
          }
        ],
        "filter": {
          "email": "gmail.com",
          "status": "ACTIVE"
        }
      }
      """;

  public static final String SEARCH_CLIENT_RESPONSE = """
      {
        "content": [
          {
            "clientId": "1",
            "firstName": "Евгений",
            "lastName": "Петров",
            "email": "petrov@example.com",
            "status": "ACTIVE"
          }
        ],
        "page": 0,
        "size": 20,
        "totalElements": 135,
        "totalPages": 7
      }
      """;

  public static final String BLOCK_CLIENT_REQUEST = """
      {
        "reason": "AML"
      }
      """;
}