import Foundation
import SwiftyJSON

public struct WithCustomIntWrapper: JSONSerializable {
    
    public let wrapper: Int?
    
    public init(
        wrapper: Int?
    ) {
        self.wrapper = wrapper
    }
    
    public static func read(json: JSON) -> WithCustomIntWrapper {
        return WithCustomIntWrapper(
            wrapper: json["wrapper"].int
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let wrapper = self.wrapper {
            json["wrapper"] = JSON(wrapper)
        }
        return JSON(json)
    }
}
