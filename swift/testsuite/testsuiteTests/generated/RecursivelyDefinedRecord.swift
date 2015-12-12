import Foundation
import SwiftyJSON

public struct RecursivelyDefinedRecord: Serializable {
    
    public let `self`: RecursivelyDefinedRecord?
    
    public init(
        `self`: RecursivelyDefinedRecord? = nil
    ) {
        self.`self` = `self`
    }
    
    public static func readJSON(json: JSON) throws -> RecursivelyDefinedRecord {
        return RecursivelyDefinedRecord(
            `self`: try json["self"].optional(.Dictionary).json.map { try RecursivelyDefinedRecord.readJSON($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `self` = self.`self` {
            dict["self"] = `self`.writeData()
        }
        return dict
    }
}
