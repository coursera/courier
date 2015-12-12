import Foundation
import SwiftyJSON

public struct CourierFile: Serializable {
    
    public let find: String?
    
    public init(
        find: String?
    ) {
        self.find = find
    }
    
    public static func readJSON(json: JSON) throws -> CourierFile {
        return CourierFile(
            find: try json["find"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let find = self.find {
            dict["find"] = find
        }
        return dict
    }
}
