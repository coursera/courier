import Foundation
import SwiftyJSON

struct DefaultLiteralEscaping {
    
    let stringField: String?
    
    init(stringField: String? = "quote: \", backslash: \\ slash: / backspace: \u{8} formfeed: \u{C} newline: \n carrage return: \r tab: \t unicode-of-ascii: B unicode-out-of-range: \u{2713}") {
        
        self.stringField = stringField
    }
    
    static func read(json: JSON) -> DefaultLiteralEscaping {
        return DefaultLiteralEscaping(
        stringField: json["stringField"].string)
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let stringField = self.stringField {
            json["stringField"] = JSON(stringField)
        }
        
        return json
    }
}
