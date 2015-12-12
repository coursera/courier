import Foundation
import SwiftyJSON

public struct DefaultLiteralEscaping: Serializable {
    
    public let stringField: String?
    
    public init(
        stringField: String? = "tripleQuote: \"\"\" "
    ) {
        self.stringField = stringField
    }
    
    public static func readJSON(json: JSON) throws -> DefaultLiteralEscaping {
        return DefaultLiteralEscaping(
            stringField: try json["stringField"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let stringField = self.stringField {
            dict["stringField"] = stringField
        }
        return dict
    }
}
