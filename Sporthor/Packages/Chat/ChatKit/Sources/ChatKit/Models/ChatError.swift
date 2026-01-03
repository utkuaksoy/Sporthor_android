//
//  ChatError.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 18.02.2025.
//
import Foundation

public enum ChatError: LocalizedError {
    case imageProcessingFailed
    case videoProcessingFailed
    case connectionFailed(Error)
    case messageSendFailed(Error)
    case fileProcessingFailed
    case invalidMessageType
    case networkError(String)
    case invalidFileType
    case fileSizeTooLarge
    case serviceNotInitialized
    
    public var errorDescription: String? {
        switch self {
        case .imageProcessingFailed:
            return "Resim işlenirken bir hata oluştu"
        case .videoProcessingFailed:
            return "Video işlenirken bir hata oluştu"
        case .connectionFailed(let error):
            return "Bağlantı hatası: \(error.localizedDescription)"
        case .messageSendFailed(let error):
            return "Mesaj gönderilemedi: \(error.localizedDescription)"
        case .fileProcessingFailed:
            return "Dosya işlenirken bir hata oluştu"
        case .invalidMessageType:
            return "Geçersiz mesaj tipi"
        case .networkError(let message):
            return "Ağ hatası: \(message)"
        case .invalidFileType:
            return "Desteklenmeyen dosya formatı"
        case .fileSizeTooLarge:
            return "Dosya boyutu çok büyük"
        case .serviceNotInitialized:
            return "Servis başlatılamadı"
        }
    }
}
