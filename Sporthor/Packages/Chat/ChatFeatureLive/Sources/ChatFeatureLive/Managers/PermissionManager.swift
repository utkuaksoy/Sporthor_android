//
//  PermissionManager.swift
//  ChatFeatureLive
//
//  Created by Mesut on 29.01.2025.
//

import AVFoundation
import Photos
import UIKit

enum PermissionType {
    case camera
    case photoLibrary
}

enum PermissionStatus {
    case authorized
    case denied
    case notDetermined
    case restricted
}

final class PermissionManager {
    static let shared = PermissionManager()
    
    private init() {}
    
    func checkPermission(_ type: PermissionType, completion: @escaping (PermissionStatus) -> Void) {
        switch type {
        case .camera:
            checkCameraPermission(completion: completion)
        case .photoLibrary:
            checkPhotoLibraryPermission(completion: completion)
        }
    }
    
    func requestPermission(_ type: PermissionType, completion: @escaping (Bool) -> Void) {
        switch type {
        case .camera:
            requestCameraPermission(completion: completion)
        case .photoLibrary:
            requestPhotoLibraryPermission(completion: completion)
        }
    }
    
    private func checkCameraPermission(completion: @escaping (PermissionStatus) -> Void) {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            completion(.authorized)
        case .denied:
            completion(.denied)
        case .notDetermined:
            completion(.notDetermined)
        case .restricted:
            completion(.restricted)
        @unknown default:
            completion(.denied)
        }
    }
    
    private func checkPhotoLibraryPermission(completion: @escaping (PermissionStatus) -> Void) {
        switch PHPhotoLibrary.authorizationStatus() {
        case .authorized, .limited:
            completion(.authorized)
        case .denied:
            completion(.denied)
        case .notDetermined:
            completion(.notDetermined)
        case .restricted:
            completion(.restricted)
        @unknown default:
            completion(.denied)
        }
    }
    
    private func requestCameraPermission(completion: @escaping (Bool) -> Void) {
        AVCaptureDevice.requestAccess(for: .video) { granted in
            DispatchQueue.main.async {
                completion(granted)
            }
        }
    }
    
    private func requestPhotoLibraryPermission(completion: @escaping (Bool) -> Void) {
        PHPhotoLibrary.requestAuthorization { status in
            DispatchQueue.main.async {
                completion(status == .authorized || status == .limited)
            }
        }
    }
}
