import Foundation
import SwiftyJSON

public struct WithInclude: Serializable {
    
    /**
        A simple field
    */
    public let message: String?
    
    public let direct: Int?
    
    public init(
        message: String? = nil,
        direct: Int?
    ) {
        self.message = message
        self.direct = direct
    }
    
    public static func readJSON(json: JSON) throws -> WithInclude {
        return WithInclude(
            message: try json["message"].optional(.String).string,
            direct: try json["direct"].optional(.Number).int
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let message = self.message {
            dict["message"] = message
        }
        if let direct = self.direct {
            dict["direct"] = direct
        }
        return dict
    }
}
