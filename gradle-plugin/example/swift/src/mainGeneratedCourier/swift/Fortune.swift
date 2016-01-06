import Foundation
import SwiftyJSON

public struct Fortune: Serializable {
    
    public let message: String?
    
    public init(
        message: String?
    ) {
        self.message = message
    }
    
    public static func readJSON(json: JSON) throws -> Fortune {
        return Fortune(
            message: try json["message"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let message = self.message {
            dict["message"] = message
        }
        return dict
    }
}
