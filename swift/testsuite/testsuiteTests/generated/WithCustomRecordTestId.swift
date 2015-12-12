import Foundation
import SwiftyJSON

public struct WithCustomRecordTestId: Serializable {
    
    public let id: Int?
    
    public init(
        id: Int?
    ) {
        self.id = id
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomRecordTestId {
        return WithCustomRecordTestId(
            id: try json["id"].optional(.Number).int
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let id = self.id {
            dict["id"] = id
        }
        return dict
    }
}
