import Foundation
import SwiftyJSON

public struct WithCustomRecord: Serializable {
    
    public let custom: Message?
    
    public let customArray: [Message]?
    
    public let customMap: [String: Message]?
    
    public init(
        custom: Message? = Message(title: "defaultTitle", body: "defaultBody"),
        customArray: [Message]? = [],
        customMap: [String: Message]? = []
    ) {
        self.custom = custom
        self.customArray = customArray
        self.customMap = customMap
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomRecord {
        return WithCustomRecord(
            custom: try json["custom"].optional(.Dictionary).json.map { try Message.readJSON($0) },
            customArray: try json["customArray"].optional(.Array).array.map { try $0.map { try Message.readJSON(try $0.required(.Dictionary).jsonValue) } },
            customMap: try json["customMap"].optional(.Dictionary).dictionary.map { try $0.mapValues { try Message.readJSON(try $0.required(.Dictionary).jsonValue) } }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let custom = self.custom {
            dict["custom"] = custom.writeData()
        }
        if let customArray = self.customArray {
            dict["customArray"] = customArray.map { $0.writeData() }
        }
        if let customMap = self.customMap {
            dict["customMap"] = customMap.mapValues { $0.writeData() }
        }
        return dict
    }
}
