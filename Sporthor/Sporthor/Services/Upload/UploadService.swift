//
//  UploadService.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 05.04.2025.
//

import Foundation
import NetworkKit
import UIKit

enum UploadService {
    case uploadImage(image: UIImage)
    case uploadMultipleImage(_ images: [UIImage])
    case videoUpload(videoURL: URL)
    case deleteImage(_ filePath: String)
    case fileUpload(fileUrl: URL)

    private static let boundary = "Boundary-\(UUID().uuidString)"
}

extension UploadService: NetworkService {
    var path: String {
        switch self {
        case .uploadImage:
            return "/api/Upload/ImageUpload"
        case .uploadMultipleImage:
            return "/api/Upload/ImageUploadMultiple"
        case .videoUpload:
            return "/api/Upload/VideoUpload"
        case .deleteImage(let filePath):
            return "/api/Upload/ImageDelete?FilePath=\(filePath)"
        case .fileUpload:
            return "/api/Upload/FileUpload"
        }
    }

    var method: HTTPMethod {
        return .POST
    }

    var headers: [String : String]? {
        switch self {
        case .uploadImage, .uploadMultipleImage, .videoUpload:
            return [
                "Content-Type": "multipart/form-data; boundary=\(UploadService.boundary)",
                "Accept": "application/json"
            ]
        case .deleteImage:
            return ["accept": "text/plain",
                    "Content-Type": "application/json",
                    "Accept-Language": UserDefaultsManager.shared.getString(forKey: "lang") ?? "tr_TR"]
        case .fileUpload:
            return [
                "Content-Type": "multipart/form-data; boundary=\(UploadService.boundary)",
                "accept": "text/plain",
                "Accept-Language": UserDefaultsManager.shared.getString(forKey: "lang") ?? "tr_TR"
            ]
        }
    }

    var httpBody: Data? {
        switch self {
        case .uploadImage(let image):
            guard let imageData = image.jpegData(compressionQuality: 0.7) else { return nil }
            var body = Data()
            body.append("--\(UploadService.boundary)\r\n")
            body.append("Content-Disposition: form-data; name=\"imageFile\"; filename=\"image.jpg\"\r\n")
            body.append("Content-Type: image/jpeg\r\n\r\n")
            body.append(imageData)
            body.append("\r\n")
            body.append("--\(UploadService.boundary)--\r\n")
            return body

        case .uploadMultipleImage(let images):
            var body = Data()
            for (index, image) in images.enumerated() {
                guard let imageData = image.jpegData(compressionQuality: 0.7) else { continue }
                body.append("--\(UploadService.boundary)\r\n")
                body.append("Content-Disposition: form-data; name=\"imageFiles\"; filename=\"image\(index).jpg\"\r\n")
                body.append("Content-Type: image/jpeg\r\n\r\n")
                body.append(imageData)
                body.append("\r\n")
            }
            body.append("--\(UploadService.boundary)--\r\n")
            return body

        case .videoUpload(let videoURL):
            guard let videoData = try? Data(contentsOf: videoURL) else { return nil }
            var body = Data()
            body.append("--\(UploadService.boundary)\r\n")
            body.append("Content-Disposition: form-data; name=\"videoFile\"; filename=\"video.mp4\"\r\n")
            body.append("Content-Type: video/mp4\r\n\r\n")
            body.append(videoData)
            body.append("\r\n")
            body.append("--\(UploadService.boundary)--\r\n")
            return body
        case .deleteImage:
            return nil
        case .fileUpload(let fileURL):
            guard let fileData = try? Data(contentsOf: fileURL) else { return nil }

            let filename = fileURL.lastPathComponent
            let mimeType = fileURL.pathExtension.mimeType

            var body = Data()
            let boundary = UploadService.boundary

            body.append("--\(boundary)\r\n")
            body.append("Content-Disposition: form-data; name=\"videoFile\"; filename=\"\(filename)\"\r\n")
            body.append("Content-Type: \(mimeType)\r\n\r\n")
            body.append(fileData)
            body.append("\r\n")
            body.append("--\(boundary)--\r\n")

            return body
        }
    }

    var token: String? {
        return ApplicationContext.shared.authToken
    }
}

extension Data {
    mutating func append(_ string: String) {
        if let data = string.data(using: .utf8) {
            append(data)
        }
    }
}

extension String {
    var mimeType: String {
        switch self.lowercased() {
        case "pdf": return "application/pdf"
        case "doc": return "application/msword"
        case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        case "xls": return "application/vnd.ms-excel"
        case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        default: return "application/octet-stream"
        }
    }
}
