---
layout: page
---

Validation
==========

Data can be validated against a schema using `ValidateDataAgainstSchema.validate()`, e.g.:

~~~ scala
val recordToValidate = new RecordToValidate(/*...*/)
val options = new ValidationOptions()
val annotationValidator = new DataSchemaAnnotationValidator(recordToValidate.schema())
val validationResult = ValidateDataAgainstSchema.validate(recordToValidate, options, annotationValidator)
if(!result.isValid()) {
  // ...
} else {
  // ...
}
~~~

This validation will check that all data is of the correct type and that all required fields are present.

To include additional validation checks using the built-in validators, such as string regex check, simply add the validation property to a field, e.g.:

{% include file_format_specific.html name="validation_example_field" %}

or to a type, e.g.:

{% include file_format_specific.html name="validation_example_typeref" %}

And then run the schema validator as usual.

To add your own custom validators, declare a new class that implements
[Validator](https://github.com/linkedin/rest.li/blob/master/data/src/main/java/com/linkedin/data/schema/validator/Validator.java), e.g.:

~~~ scala
class CustomValidator(config: DataMap) extends Validator {
  val property = config.getString("customValidatorProperty");

  def validate(context: ValidatorContext): Unit = {

    context.dataElement.getValue match {
      case str: String =>
        if (!someCondition) {
          context.addResult(new Message(
            element.path, true, s"Validation failure message goes here")
        } else {
          // valid!
        }
      case _: Any =>
        context.addResult(new Message(
          element.path, true, s"CustomValidator may only be applied to strings.")
    }
  }
}
~~~

And then use the validator in schemas.

{% include file_format_specific.html name="validation_custom_example" %}

It is also possible to assign a validator a short name. To do this,
use the constructor of [DataSchemaAnnotationValidator](https://github.com/linkedin/rest.li/blob/master/data/src/main/java/com/linkedin/data/schema/validator/DataSchemaAnnotationValidator.java), e.g.:

And then include the custom validator when validating:

~~~ scala
val customValidator = new CustomValidator();

val recordToValidate = new RecordToValidate(/*...*/)
val options = new ValidationOptions()
val validator =
  new DataSchemaAnnotationValidator(recordToValidate, Map("custom" -> customValidator))
val validationResult =
  ValidateDataAgainstSchema.validate(recordToValidate, options, validator)
// ...
~~~

And then the validator can be referenced in schemas using the short 'custom' name
instead of the fully qualified 'org.example.CustomValidator' class name.

For more details on validation, see [Pegasus schema validation](https://github.com/linkedin/rest.li/wiki/DATA-Data-Schema-and-Templates#data-to-schema-validation).
