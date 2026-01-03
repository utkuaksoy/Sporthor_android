//
//  FileModel.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//

import Foundation

struct FileModel: Codable {
    var name: String?
    var url: URL?
    var filePath: String?
    
    init(name: String? = nil, url: URL? = nil, filePath: String? = nil) {
        self.name = name
        self.url = url
        self.filePath = filePath
    }
}
