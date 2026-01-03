//
//  Extension+AVAsset.swift
//  ComponentKit
//
//  Created by derTurke on 27.04.2025.
//

import AVFoundation

public extension AVAsset {
    var isPortrait: Bool {
        guard let track = self.tracks(withMediaType: .video).first else { return false }
        let size = track.naturalSize.applying(track.preferredTransform)
        return abs(size.height) > abs(size.width)
    }
    
    var isLandscape: Bool {
        guard let track = self.tracks(withMediaType: .video).first else { return false }
        let size = track.naturalSize.applying(track.preferredTransform)
        return abs(size.width) > abs(size.height)
    }
}
