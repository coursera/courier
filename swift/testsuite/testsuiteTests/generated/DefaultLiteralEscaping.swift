import Foundation
import SwiftyJSON

public struct DefaultLiteralEscaping: JSONSerializable {
    
    public let stringField: String?
    
    public init(
        stringField: String? = "quote: \", backslash: \\ slash: / backspace: \u{8} formfeed: \u{C} newline: \n carrage return: \r tab: \t unicode-of-ascii: B unicode-out-of-range: \u{2713}"
    ) {
        self.stringField = stringField
    }
    
    public static func read(json: JSON) -> DefaultLiteralEscaping {
        return DefaultLiteralEscaping(
            stringField: json["stringField"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let stringField = self.stringField {
            json["stringField"] = JSON(stringField)
        }
        return JSON(json)
    }
}
