//
//  StorySettingViewController.swift
//  Sporthor
//
//  Created by derTurke on 11.05.2025.
//
//

import UIKit
import PanModal

final class StorySettingViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: StorySettingPresenterProtocol {
        get { return self.basePresenter as! StorySettingPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = .black
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .insetGrouped)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.backgroundColor = DesignKitColorName.backgroundWeak100.color
        tableView.translatesAutoresizingMaskIntoConstraints = false
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

// MARK: - StorySettingPresenterDelegate
extension StorySettingViewController: StorySettingPresenterDelegate {
    override func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
    }
    
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            tableView.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - UITableViewDataSource
extension StorySettingViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let item = presenter.items[indexPath.row]
        let cell = SettingsCell.dequeue(from: tableView, at: indexPath)
        cell.configure(with: item, isDark: true)
        return cell
    }
}

// MARK: - UITableViewDelegate
extension StorySettingViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRow(at: indexPath)
    }
}

// MARK: - PanModalPresentable
extension StorySettingViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return tableView
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

