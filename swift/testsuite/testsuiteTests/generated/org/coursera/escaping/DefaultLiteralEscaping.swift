import Foundation
import SwiftyJSON

struct DefaultLiteralEscaping {
    
    let stringField: String?
    
    init(stringField: String? = "tripleQuote: \"\"\" ") {
        
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
