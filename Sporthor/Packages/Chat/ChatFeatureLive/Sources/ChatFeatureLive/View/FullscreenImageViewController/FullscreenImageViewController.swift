//
//  FullscreenImageViewController.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 21.02.2025.
//

import AutoLayout
import UIKit

final class FullscreenImageViewController: UIViewController {
    
    // MARK: - Private Properties
    
    private let image: UIImage

    // MARK: - UI Components
    
    private lazy var dismissButton: UIButton = {
        let button = UIButton(type: .system)
        let config = UIImage.SymbolConfiguration(pointSize: 20, weight: .medium)
        let image = UIImage(systemName: "xmark.circle.fill", withConfiguration: config)
        button.setImage(image, for: .normal)
        button.tintColor = .white
        button.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        button.layer.cornerRadius = 15
        button.addTarget(self, action: #selector(dismissTapped), for: .touchUpInside)
        return button
    }()
    
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        scroll.minimumZoomScale = 1.0
        scroll.maximumZoomScale = 4.0
        scroll.showsVerticalScrollIndicator = false
        scroll.showsHorizontalScrollIndicator = false
        scroll.contentInsetAdjustmentBehavior = .never
        scroll.delegate = self
        return scroll
    }()
    
    private lazy var imageView: UIImageView = {
        let view = UIImageView(image: image)
        view.contentMode = .scaleAspectFit
        return view
    }()
    
    // MARK: - Initializer
    
    init(image: UIImage) {
        self.image = image
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    deinit {
        print("\(self) deinit ✅")
    }
    
    // MARK: - Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .black
        setupUI()
        setupGestures()
    }

    override var prefersStatusBarHidden: Bool {
        return true
    }
    
    // MARK: - Setup
    
    private func setupUI() {
        view.addSubview(scrollView) {
            $0.pin(to: view)
        }
        scrollView.addSubview(imageView) {
            $0.width == scrollView.widthAnchor
            $0.height == scrollView.heightAnchor
            $0.centerX == scrollView.centerXAnchor
            $0.centerY == scrollView.centerYAnchor
        }
        view.addSubview(dismissButton) {
            $0.top == view.safeAreaLayoutGuide.topAnchor + 16
            $0.trailing == view.trailingAnchor - 16
            $0.width == 30
            $0.height == 30
        }
    }
    
    private func setupGestures() {
        let singleTap = UITapGestureRecognizer(target: self, action: #selector(handleSingleTap))
        let doubleTap = UITapGestureRecognizer(target: self, action: #selector(handleDoubleTap))
        doubleTap.numberOfTapsRequired = 2
        singleTap.require(toFail: doubleTap)
        
        view.addGestureRecognizer(singleTap)
        view.addGestureRecognizer(doubleTap)
    }
    
    // MARK: - Actions
    
    @objc
    private func handleSingleTap() {
        dismiss(animated: true)
    }
    
    @objc
    private func handleDoubleTap(_ gesture: UITapGestureRecognizer) {
        let location = gesture.location(in: imageView)
        let zoomRect = CGRect(x: location.x - 50, y: location.y - 50, width: 100, height: 100)
        
        if scrollView.zoomScale > scrollView.minimumZoomScale {
            scrollView.setZoomScale(scrollView.minimumZoomScale, animated: true)
        } else {
            scrollView.zoom(to: zoomRect, animated: true)
        }
    }

    @objc
    private func dismissTapped() {
        dismiss(animated: true)
    }
}

// MARK: - UIScrollViewDelegate

extension FullscreenImageViewController: UIScrollViewDelegate {
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        return imageView
    }
}
