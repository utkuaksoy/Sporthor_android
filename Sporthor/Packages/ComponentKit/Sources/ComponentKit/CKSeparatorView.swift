//
//  CKSeparatorView.swift
//  ComponentKit
//
//  Created by derTurke on 5.06.2025.
//

import UIKit
import DesignKit

public final class CKSeparatorView: UIView {
    private lazy var separatorView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    public init(color: UIColor = ColorName.borderSoft200.color,
                height: CGFloat = 1,
                leadingCons: CGFloat = 16,
                trailingCons: CGFloat = -16) {
        super.init(frame: .zero)
        bind(color: color,
             height: height,
             leadingCons: leadingCons,
             trailingCons: trailingCons)
    }
    
    public required init?(coder: NSCoder) {
        super.init(coder: coder)
        bind()
    }
    
    public func bind(color: UIColor = ColorName.borderSoft200.color,
                     height: CGFloat = 1,
                     leadingCons: CGFloat = 16,
                     trailingCons: CGFloat = -16) {
        separatorView.backgroundColor = color
        
        setupView(height: height, leadingCons: leadingCons, trailingCons: trailingCons)
    }
    
    private func setupView(height: CGFloat = 1,
                           leadingCons: CGFloat = 16,
                           trailingCons: CGFloat = -16) {
        addSubview(separatorView)
        NSLayoutConstraint.activate([
            separatorView.heightAnchor.constraint(equalToConstant: height),
            separatorView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: leadingCons),
            separatorView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: trailingCons),
            separatorView.centerYAnchor.constraint(equalTo: centerYAnchor)
        ])
    }
}
