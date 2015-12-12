import Foundation
import SwiftyJSON

public struct WithInclude: Serializable {
    
    public let find: String?
    
    public let direct: Int?
    
    public init(
        find: String?,
        direct: Int?
    ) {
        self.find = find
        self.direct = direct
    }
    
    public static func readJSON(json: JSON) throws -> WithInclude {
        return WithInclude(
            find: try json["find"].optional(.String).string,
            direct: try json["direct"].optional(.Number).int
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let find = self.find {
            dict["find"] = find
        }
        if let direct = self.direct {
            dict["direct"] = direct
        }
        return dict
    }
}
