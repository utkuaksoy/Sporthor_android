//
//  CKWormPageControl.swift
//  ComponentKit
//
//  Created by derTurke on 23.02.2025.
//

import UIKit

public final class CKWormPageControl: UIView {

    // MARK: - Members
    private var numberOfPages: Int
    public var currentPage: Int {
        didSet {
            updateCurrentPage(animated: true)
        }
    }
    
    private lazy var pageDots: [UIView] = []
    private lazy var wormView = UIView()
    
    private var pageTintColor: UIColor
    private var currentPageTintColor: UIColor
    private var animationDuration: TimeInterval
    private var dotSize: CGFloat
    private var wormSize: CGFloat
    private var dotSpacing: CGFloat
    private let maxVisibleDots: Int
    private var visibleRangeStartIndex: Int {
        return max(0, min(currentPage - maxVisibleDots / 2, numberOfPages - maxVisibleDots))
    }
    
    // MARK: - Initialize
    public init(numberOfPages: Int = 0,
                currentPage: Int = 0,
                tintColor: UIColor = .clear,
                currentPageTintColor: UIColor = .clear,
                dotSize: CGFloat = 0,
                wormSize: CGFloat = 0,
                dotSpacing: CGFloat = 0,
                animationDuration: TimeInterval = 0.2,
                maxVisibleDots: Int = 5) {
        self.numberOfPages = numberOfPages
        self.currentPage = currentPage
        self.pageTintColor = tintColor
        self.currentPageTintColor = currentPageTintColor
        self.animationDuration = animationDuration
        self.dotSize = dotSize
        self.wormSize = wormSize
        self.dotSpacing = dotSpacing
        self.maxVisibleDots = maxVisibleDots
        super.init(frame: .zero)
        setupWormView()
        setupPages()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Custom Methods
    private func setupWormView() {
        wormView.backgroundColor = currentPageTintColor
        wormView.setCornerRadius(wormSize / 2)
        addSubview(wormView)
    }
    
    private func setupPages() {
        pageDots.forEach { $0.removeFromSuperview() }
        pageDots.removeAll()
        for _ in 0..<numberOfPages {
            let dot = UIView()
            dot.backgroundColor = pageTintColor
            dot.setCornerRadius(dotSize / 2)
            addSubview(dot)
            pageDots.append(dot)
        }
        updateCurrentPage(animated: false)
        setNeedsLayout()
    }
    
    private func updateCurrentPage(animated: Bool) {
        guard numberOfPages > 0 else { return }
        
        let visibleStart = visibleRangeStartIndex
        let visibleEnd = min(visibleStart + maxVisibleDots, numberOfPages)
        
        let visibleDots = pageDots[visibleStart..<visibleEnd]
        let totalSpacing = CGFloat(visibleDots.count - 1) * dotSpacing
        let totalDotWidth = CGFloat(visibleDots.count) * dotSize
        let totalWidth = totalDotWidth + totalSpacing
        var x = (bounds.width - totalWidth) / 2
        
        for (i, dot) in pageDots.enumerated() {
            let isVisible = i >= visibleStart && i < visibleEnd
            let isCurrent = i == currentPage
            dot.isHidden = !(isVisible && !isCurrent)
            
            guard isVisible else { continue }
            
            dot.frame = CGRect(x: x, y: 0, width: dotSize, height: dotSize)
            dot.layer.cornerRadius = dotSize / 2
            x += dotSize + dotSpacing
        }
        
        let wormIndex = currentPage
        if wormIndex >= visibleStart && wormIndex < visibleEnd {
            let targetDot = pageDots[wormIndex]
            let targetFrame = targetDot.frame
            let wormTargetFrame = CGRect(
                x: targetFrame.midX - wormSize / 2,
                y: targetFrame.origin.y,
                width: wormSize,
                height: dotSize
            )
            
            wormView.layer.cornerRadius = dotSize / 2
            wormView.isHidden = false
            
            if animated {
                UIView.animate(withDuration: animationDuration, animations: {
                    self.wormView.frame = wormTargetFrame
                })
            } else {
                wormView.frame = wormTargetFrame
            }
        } else {
            wormView.isHidden = true
        }
    }
    
    public override func layoutSubviews() {
        super.layoutSubviews()
        updateCurrentPage(animated: false)
    }
    
    public func setNumberOfPages(_ pages: Int) {
        self.numberOfPages = pages
        setupWormView()
        setupPages()
    }
    
    public func setCurrentPage(_ page: Int) {
        self.currentPage = page
    }
    
    public override var intrinsicContentSize: CGSize {
        let totalSpacing = CGFloat(min(numberOfPages, maxVisibleDots) - 1) * dotSpacing
        let totalDotWidth = CGFloat(min(numberOfPages, maxVisibleDots) - 1) * dotSize + wormSize
        let width = totalDotWidth + totalSpacing
        return CGSize(width: width, height: dotSize)
    }
}
