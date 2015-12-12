import Foundation
import SwiftyJSON

public struct WithCustomMapTestIds: Serializable {
    
    public let map: [String: Int]?
    
    public init(
        map: [String: Int]?
    ) {
        self.map = map
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomMapTestIds {
        return WithCustomMapTestIds(
            map: try json["map"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.Number).intValue } }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let map = self.map {
            dict["map"] = map
        }
        return dict
    }
}
