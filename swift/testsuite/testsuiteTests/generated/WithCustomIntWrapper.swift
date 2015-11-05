import Foundation
import SwiftyJSON

public struct WithCustomIntWrapper: JSONSerializable, DataTreeSerializable {
    
    public let wrapper: Int?
    
    public init(
        wrapper: Int?
    ) {
        self.wrapper = wrapper
    }
    
    public static func readJSON(json: JSON) -> WithCustomIntWrapper {
        return WithCustomIntWrapper(
            wrapper: json["wrapper"].int
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithCustomIntWrapper {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let wrapper = self.wrapper {
            dict["wrapper"] = wrapper
        }
        return dict
    }
}
