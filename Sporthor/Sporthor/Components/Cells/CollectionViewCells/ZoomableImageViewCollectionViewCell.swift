//
//  ZoomableImageViewCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 16.06.2025.
//

import UIKit
import ComponentKit

protocol ZoomableImageViewCollectionViewCellDelegate: AnyObject {
    func zooming(started: Bool)
}

final class ZoomableImageViewCollectionViewCell: UICollectionViewCell {
    
    // MARK: - UI
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.clipsToBounds = true
        imageView.contentMode = .scaleAspectFill
        imageView.isUserInteractionEnabled = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    // MARK: - Gesture-related
    private weak var delegate: ZoomableImageViewCollectionViewCellDelegate?
    private var overlayView: UIView?
    private var windowImageView: UIImageView?
    private var startingFrame = CGRect.zero
    private var fixedPinchCenter: CGPoint = .zero
    private var lastPanPoint: CGPoint = .zero
    
    // MARK: - Constants
    private let maxOverlayAlpha: CGFloat = 0.8
    private let minOverlayAlpha: CGFloat = 0.4
    private let animationDuration: TimeInterval = 0.3
    
    // MARK: - Init
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        contentView.addSubview(imageView)
        NSLayoutConstraint.activate([
            imageView.topAnchor.constraint(equalTo: contentView.topAnchor),
            imageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            imageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            imageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
        
        let pinch = UIPinchGestureRecognizer(target: self, action: #selector(handlePinch(_:)))
        pinch.delegate = self
        imageView.addGestureRecognizer(pinch)
        
        let pan = UIPanGestureRecognizer(target: self, action: #selector(handlePan(_:)))
        pan.delegate = self
        imageView.addGestureRecognizer(pan)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Binding
    func bind(_ url: String, delegate: ZoomableImageViewCollectionViewCellDelegate? = nil) {
        self.delegate = delegate
        imageView.setImage(with: url)
    }
    
    // MARK: - Pinch Logic
    @objc private func handlePinch(_ gesture: UIPinchGestureRecognizer) {
        guard let window = UIApplication.shared.windows.first(where: { $0.isKeyWindow }) else { return }

        switch gesture.state {
        case .began:
            guard gesture.scale > 1 else { return }
            delegate?.zooming(started: true)
            
            let overlay = UIView(frame: window.bounds)
            overlay.backgroundColor = .black
            overlay.alpha = minOverlayAlpha
            window.addSubview(overlay)
            self.overlayView = overlay
            
            let image = imageView.image ?? UIImage()
            let tempImageView = UIImageView(image: image)
            tempImageView.contentMode = .scaleAspectFit
            tempImageView.clipsToBounds = true
            
            startingFrame = imageView.convert(imageView.bounds, to: window)
            tempImageView.frame = startingFrame
            window.addSubview(tempImageView)
            self.windowImageView = tempImageView
            
            imageView.isHidden = true
            fixedPinchCenter = gesture.location(in: window)
            
        case .changed:
            guard let tempImageView = windowImageView else { return }
            
            let currentScale = tempImageView.frame.size.width / startingFrame.size.width
            var newScale = currentScale * gesture.scale
            newScale = max(1.0, min(newScale, 3.0))
            
            tempImageView.transform = .identity
            
            let anchor = fixedPinchCenter
            let dx = anchor.x - tempImageView.center.x
            let dy = anchor.y - tempImageView.center.y
            
            var transform = CGAffineTransform.identity
            transform = transform.translatedBy(x: dx, y: dy)
            transform = transform.scaledBy(x: newScale, y: newScale)
            transform = transform.translatedBy(x: -dx, y: -dy)
            tempImageView.transform = transform
            
            gesture.scale = 1.0
            
            overlayView?.alpha = min(minOverlayAlpha + (newScale - 1), maxOverlayAlpha)
            
        case .ended, .cancelled, .failed:
            animateZoomReset()
            
        default:
            break
        }
    }
    
    // MARK: - Pan Logic
    @objc private func handlePan(_ gesture: UIPanGestureRecognizer) {
        guard let imageView = windowImageView else { return }

        let translation = gesture.translation(in: imageView.superview)

        switch gesture.state {
        case .changed:
            imageView.center = CGPoint(
                x: imageView.center.x + translation.x,
                y: imageView.center.y + translation.y
            )
            gesture.setTranslation(.zero, in: imageView.superview)

        default:
            break
        }
    }
    
    private func animateZoomReset() {
        guard let tempImageView = windowImageView else { return }
        
        UIView.animate(withDuration: animationDuration, animations: {
            tempImageView.transform = .identity
            tempImageView.frame = self.startingFrame
            self.overlayView?.alpha = 0
        }, completion: { _ in
            tempImageView.removeFromSuperview()
            self.overlayView?.removeFromSuperview()
            self.imageView.isHidden = false
            self.delegate?.zooming(started: false)
        })
    }
}

// MARK: - UIGestureRecognizerDelegate
extension ZoomableImageViewCollectionViewCell: UIGestureRecognizerDelegate {
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer,
                           shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
}
