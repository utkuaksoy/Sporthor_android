//
//  SelectionViewController.swift
//  Sporthor
//
//  Created by derTurke on 14.04.2025.
//
//

import UIKit
import ComponentKit
import PanModal

final class SelectionViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: SelectionPresenterProtocol {
        get { return self.basePresenter as! SelectionPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .plain)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.backgroundColor = .clear
        tableView.separatorColor = DesignKitColorName.borderSub300.color
        tableView.separatorInset = UIEdgeInsets(top: 0, left: 16, bottom: 0, right: 16)
        tableView.removeEmptyCell()
        return tableView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - SelectionPresenterDelegate
extension SelectionViewController: SelectionPresenterDelegate {
    func didSetCustomTitle(_ title: String) {
        titleLabel.text = title
    }
    
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(titleLabel)
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            titleLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),

            tableView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
}

// MARK: - UITableViewDataSource
extension SelectionViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.model.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = SelectionTableViewCell.dequeue(from: tableView, at: indexPath)
        cell.bind(with: presenter.model[indexPath.row])
        return cell
    }
}

// MARK: - UITableViewDelegate
extension SelectionViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        dismiss(animated: true) { [weak self] in
            guard let self else { return }
            self.presenter.didSelectItemAt(indexPath)
        }
    }
}

// MARK: - PanModalPresentable
extension SelectionViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return tableView
    }
    
    var longFormHeight: PanModalHeight {
        return .maxHeight
    }
    
    var shortFormHeight: PanModalHeight {
        return .contentHeight(250)
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }

    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
}
