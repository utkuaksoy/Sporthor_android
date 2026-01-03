//
//  CircularCountdownCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 22.02.2025.
//

import UIKit
import ComponentKit

final class CircularCountdownCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var circularCountdownView: CKCircularCountdownView = {
        let view = CKCircularCountdownView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Members
    
    // MARK: Initialize
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        contentView.addSubview(circularCountdownView)
        
        NSLayoutConstraint.activate([
            circularCountdownView.topAnchor.constraint(equalTo: contentView.topAnchor),
            circularCountdownView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            circularCountdownView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            circularCountdownView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
        
    }
    
    // MARK: - Custom Methods
    func bind(delegate: CKCircularCountdownViewDelegate? = nil,
              backgroundLayerColor: UIColor = .clear,
              shapeLayerColor: UIColor = .clear,
              changeShapeLayerColor: UIColor = .clear,
              lineWidth: CGFloat = 0,
              timeTextColor: UIColor = DesignKitColorName.contentStrong900.color,
              timeFont: UIFont? = .body04Compact,
              duration: Int = 0,
              changeColorDuration: Int = 0) {
        circularCountdownView.configure(
            delegate: delegate,
            backgroundLayerColor: backgroundLayerColor,
            shapeLayerColor: shapeLayerColor,
            changeShapeLayerColor: changeShapeLayerColor,
            lineWidth: lineWidth,
            timeTextColor: timeTextColor,
            timeFont: timeFont,
            duration: duration,
            changeColorDuration: changeColorDuration)
    }
}
