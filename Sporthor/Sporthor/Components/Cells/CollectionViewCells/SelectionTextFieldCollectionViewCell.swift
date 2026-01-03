//
//  SelectionTextFieldCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 16.07.2025.
//

import UIKit
import ComponentKit

final class SelectionTextFieldCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var selectionTextField: CKSelectionTextField = {
        let selectionTextField = CKSelectionTextField()
        selectionTextField.translatesAutoresizingMaskIntoConstraints = false
        return selectionTextField
    }()
    
    // MARK: - Initialize
    // MARK: - Initialize
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(selectionTextField)
        
        NSLayoutConstraint.activate([
            selectionTextField.topAnchor.constraint(equalTo: contentView.topAnchor),
            selectionTextField.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            selectionTextField.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            selectionTextField.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    func bind(delegate: CKSelectionTextFieldDelegate? = nil,
              items: [String],
              text: String = "",
              placeholder: String) {
        selectionTextField.bind(delegate: delegate, items: items, text: text, placeholder: placeholder)
    }
}
